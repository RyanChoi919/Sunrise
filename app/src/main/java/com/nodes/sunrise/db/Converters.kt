package com.nodes.sunrise.db

import android.location.Address
import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.nodes.sunrise.model.weather.WeatherInfo
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.streams.toList

class Converters {

    @TypeConverter
    fun fromLongToLocalDateTime(epochSecond: Long?): LocalDateTime? {
        return if (epochSecond == null) null else LocalDateTime.ofEpochSecond(
            epochSecond,
            0,
            OffsetDateTime.now().offset
        )
    }

    @TypeConverter
    fun fromLocalDateTimeToLong(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.toEpochSecond(OffsetDateTime.now().offset)
    }

    @TypeConverter
    fun fromGsonToUriList(value: String?): List<Uri>? {
        return if (value == null) {
            null
        } else {
            val stringList = Gson().fromJson(value, Array<String>::class.java).toList()
            stringList.stream().map { string -> Uri.parse(string) }.toList()
        }
    }

    @TypeConverter
    fun fromUriListToGson(list: List<Uri>?): String? {
        return if (list == null) {
            null
        } else {
            Gson().toJson(list.stream().map { uri -> uri.toString() }.toList())
        }
    }

    @TypeConverter
    fun fromWeatherInfoToGson(weatherInfo: WeatherInfo?): String? {
        return if (weatherInfo == null) {
            null
        } else {
            Gson().toJson(weatherInfo)
        }
    }

    @TypeConverter
    fun fromGsonToWeatherInfo(value: String?): WeatherInfo? {
        return if (value == null) {
            null
        } else {
            Gson().fromJson(value, WeatherInfo::class.java)
        }
    }

    @TypeConverter
    fun fromAddressToGson(address: Address?): String? {
        return if (address == null) {
            null
        } else {
            Gson().toJson(address)
        }
    }

    @TypeConverter
    fun fromGsonToAddress(value: String?): Address? {
        return if (value == null) {
            null
        } else {
            Gson().fromJson(value, Address::class.java)
        }
    }
}