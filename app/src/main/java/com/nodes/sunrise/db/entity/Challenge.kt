package com.nodes.sunrise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String
)