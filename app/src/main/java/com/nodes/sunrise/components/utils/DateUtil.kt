package com.nodes.sunrise.components.utils

import android.content.Context
import java.time.DayOfWeek
import java.time.YearMonth
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

        private fun getCurrentLocale(context: Context): Locale {
            return context.resources.configuration.locales[0]!!
        }
    }
}