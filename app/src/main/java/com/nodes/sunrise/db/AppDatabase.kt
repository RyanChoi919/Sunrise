package com.nodes.sunrise.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nodes.sunrise.components.utils.CsvUtil
import com.nodes.sunrise.db.dao.ChallengeAndGroupCrossRefDao
import com.nodes.sunrise.db.dao.ChallengeDao
import com.nodes.sunrise.db.dao.ChallengeGroupDao
import com.nodes.sunrise.db.dao.EntryDao
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.ChallengeAndGroupCrossRef
import com.nodes.sunrise.db.entity.ChallengeGroup
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.AssetNames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Entry::class, Challenge::class, ChallengeGroup::class, ChallengeAndGroupCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract val entryDao: EntryDao
    abstract val challengeDao: ChallengeDao
    abstract val challengeGroupDao: ChallengeGroupDao
    abstract val challengeAndGroupCrossRefDao: ChallengeAndGroupCrossRefDao

    /* 앱 최초 실행 시 데이터베이스 초기화 콜백 클래스 */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { appDatabase ->
                scope.launch {
                    val challengeList = CsvUtil(context).readAllFromCsv(AssetNames.CSV_CHALLENGE.fileName)
                    val challengeGroupList =
                        CsvUtil(context).readAllFromCsv(AssetNames.CSV_CHALLENGE_GROUP.fileName)
                    val challengeAndGroupCrossRefList =
                        CsvUtil(context).readAllFromCsv(AssetNames.CSV_CHALLENGE_AND_GROUP_CROSS_REF.fileName)

                    with(appDatabase) {
                        addListToDatabase(challengeList, challengeDao)
                        addListToDatabase(challengeGroupList, challengeGroupDao)
                        addListToDatabase(
                            challengeAndGroupCrossRefList,
                            challengeAndGroupCrossRefDao
                        )
                    }
                }
            }
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    AppDatabase::class.java.simpleName
                )
                    .addCallback(AppDatabaseCallback(scope, context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private inline fun <reified T> addListToDatabase(list: List<Array<String>>, dao: T) {
        for (i in 1 until list.count()) {
            when (T::class) {
                ChallengeDao::class -> {
                    (dao as ChallengeDao).insert(Challenge(0, list[i][1]))
                }
                ChallengeGroupDao::class -> {
                    (dao as ChallengeGroupDao).insert(ChallengeGroup(0, list[i][1]))
                }
                ChallengeAndGroupCrossRefDao::class -> {
                    (dao as ChallengeAndGroupCrossRefDao).insert(
                        ChallengeAndGroupCrossRef(
                            Integer.valueOf(list[i][0]),
                            Integer.valueOf(list[i][1])
                        )
                    )
                }
            }
        }
    }
}