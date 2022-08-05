package com.nodes.sunrise.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nodes.sunrise.components.utils.DevUtil
import com.nodes.sunrise.db.dao.ChallengeDao
import com.nodes.sunrise.db.dao.EntryDao
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Entry::class, Challenge::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
internal abstract class AppDatabase : RoomDatabase() {

    abstract val entryDao: EntryDao
    abstract val challengeDao: ChallengeDao

    /* 개발을 위한 데이터베이스 초기화 콜백 클래스 */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { appDatabase ->
                scope.launch {
                    DevUtil.createSampleChallenges(30).stream().forEach {
                        appDatabase.challengeDao.insert(it)
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
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}