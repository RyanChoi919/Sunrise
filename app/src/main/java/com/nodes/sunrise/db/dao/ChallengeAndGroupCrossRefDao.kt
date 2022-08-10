package com.nodes.sunrise.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.nodes.sunrise.db.entity.ChallengeAndGroupCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface ChallengeAndGroupCrossRefDao: BaseDao<ChallengeAndGroupCrossRef> {
}