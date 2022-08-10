package com.nodes.sunrise.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.ChallengeAndGroupCrossRef
import com.nodes.sunrise.db.entity.ChallengeGroup
import com.nodes.sunrise.db.entity.ChallengeGroupWithChallenges
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeDao : BaseDao<Challenge> {
    @Query("DELETE FROM Challenge")
    fun deleteAll()

    @Query("SELECT * FROM Challenge ORDER BY challengeId")
    fun getAllChallengesOrderById(): Flow<List<Challenge>>

    @Query("SELECT * FROM Challenge WHERE challengeId = :id ORDER BY challengeId")
    fun getChallengeById(id: Int): Flow<Challenge>
}