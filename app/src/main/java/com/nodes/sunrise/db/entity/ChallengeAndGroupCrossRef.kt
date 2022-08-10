package com.nodes.sunrise.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["challengeId", "challengeGroupId"])
data class ChallengeAndGroupCrossRef(
    val challengeId: Int,
    @ColumnInfo(index = true)
    val challengeGroupId: Int
)