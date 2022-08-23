package com.nodes.sunrise.components.utils

import android.content.Context
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

class DateUtil() {

    companion object {
        fun getLocalizedDayOfWeekString(context: Context, dow: DayOfWeek): String {
            return dow.getDisplayName(TextStyle.SHORT, getCurrentLocale(context))
        }

        fun getLocalizedMonthString(context: Context, yearMonth: YearMonth): String {
            return yearMonth.month.getDisplayName(TextStyle.SHORT, getCurrentLocale(context))
        }

        fun getLocalizedMonthAndDayOfMonthString(date: LocalDate): String {
            return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
        }

        fun getLocalizedTimeString(time: LocalTime): String {
            return time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        }

        private fun getCurrentLocale(context: Context): Locale {
            return context.resources.configuration.locales[0]!!
        }
    }
}