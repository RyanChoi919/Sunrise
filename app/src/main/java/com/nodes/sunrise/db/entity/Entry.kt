package com.nodes.sunrise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var dateTime: LocalDateTime,
    var title: String,
    var isTitleEnabled: Boolean,
    var content: String
)
