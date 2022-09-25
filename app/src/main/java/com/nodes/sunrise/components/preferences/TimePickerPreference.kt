package com.nodes.sunrise.components.preferences

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.nodes.sunrise.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TimePickerPreference(context: Context, attr: AttributeSet) : DialogPreference(context, attr) {

    var time: LocalTime? = null
        set(value) {
            field = value
            if (value != null) {
                persistInt(value.withSecond(0).toSecondOfDay())
                setSummary(value)
            }
        }
    private var dialogLayoutResId = R.layout.pref_dialog_time_picker

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInt(index, 0)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        time = if (defaultValue != null) {
            LocalTime.ofSecondOfDay((defaultValue as Int).toLong())
        } else {
            val secondsAfterMidnight =
                getPersistedInt(
                    LocalTime.now().withHour(22).withMinute(0).withSecond(0).toSecondOfDay()
                )
            LocalTime.ofSecondOfDay(secondsAfterMidnight.toLong())
        }
    }

    override fun getDialogLayoutResource(): Int {
        return dialogLayoutResId
    }

    private fun setSummary(time: LocalTime) {
        val formattedTime = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(time)
        summary = formattedTime
    }
}