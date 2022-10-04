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
    var id: Int = 0,
    var dateTime: LocalDateTime = LocalDateTime.now(),
    var title: String = "",
    var isTitleEnabled: Boolean = true,
    var content: String = "",
    var latitude: Double? = null,
    var longitude: Double? = null,
    var weatherInfo: WeatherInfo? = null,
    var photos: List<Uri> = emptyList()
) : Serializable