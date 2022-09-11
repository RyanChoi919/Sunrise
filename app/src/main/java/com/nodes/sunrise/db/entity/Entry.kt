package com.nodes.sunrise.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nodes.sunrise.model.weather.WeatherInfo
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
    var longitude: Double?,
    var weatherInfo: WeatherInfo?,
    var photos: List<Uri> = emptyList()
) : Serializable

class EntryFactory {
    companion object {
        fun create(): Entry {
            return Entry(0, LocalDateTime.now(), "", true, "", null, null, null)
        }
    }
}
