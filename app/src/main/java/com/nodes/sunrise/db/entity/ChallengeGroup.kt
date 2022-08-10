package com.nodes.sunrise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChallengeGroup (
    @PrimaryKey(autoGenerate = true)
    var challengeGroupId : Int,
    var name: String,
)