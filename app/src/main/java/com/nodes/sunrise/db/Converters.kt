package com.nodes.sunrise.db

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.OffsetDateTime

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
}