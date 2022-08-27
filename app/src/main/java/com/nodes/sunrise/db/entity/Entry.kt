package com.nodes.sunrise.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

@Entity
data class Entry(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var dateTime: LocalDateTime,
    var title: String,
    var isTitleEnabled: Boolean,
    var content: String,
    var latitude: Double?,
    var longitude: Double?
) : Serializable

class EntryFactory {
    companion object {
        fun create(): Entry {
            return Entry(0, LocalDateTime.now(), "", true, "", null, null)
        }
    }
}
