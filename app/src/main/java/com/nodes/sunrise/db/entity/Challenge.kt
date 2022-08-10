package com.nodes.sunrise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    var challengeId: Int,
    var name: String
) : Serializable