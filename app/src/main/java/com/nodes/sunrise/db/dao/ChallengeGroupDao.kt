package com.nodes.sunrise.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nodes.sunrise.db.entity.ChallengeGroup
import com.nodes.sunrise.db.entity.ChallengeGroupWithChallenges
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeGroupDao : BaseDao<ChallengeGroup> {

    @Transaction
    @Query("SELECT * FROM ChallengeGroup")
    fun getChallengeGroupsWithChallenges(): Flow<List<ChallengeGroupWithChallenges>>
}