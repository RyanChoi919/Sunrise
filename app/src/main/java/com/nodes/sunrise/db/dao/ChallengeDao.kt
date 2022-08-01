package com.nodes.sunrise.db.dao

import android.os.FileObserver.DELETE
import androidx.room.Dao
import androidx.room.Query
import com.nodes.sunrise.db.entity.Challenge
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao: BaseDao<Challenge> {
    @Query("DELETE FROM Challenge")
    override fun deleteAll()

    @Query("SELECT * FROM Challenge ORDER BY id")
    override fun getAllEntitiesOrderById(): Flow<List<Challenge>>

    @Query("SELECT * FROM Challenge WHERE id = :id ORDER BY id")
    override fun getEntityById(id: Int): Flow<Challenge>
}