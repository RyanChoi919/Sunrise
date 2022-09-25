package com.nodes.sunrise.components.preferences

import android.os.Bundle
import android.view.View
import androidx.preference.DialogPreference
import androidx.preference.PreferenceDialogFragmentCompat
import com.nodes.sunrise.databinding.PrefDialogTimePickerBinding
import java.time.LocalTime


class TimePickerPreferenceDialogFragmentCompat : PreferenceDialogFragmentCompat() {

    private var _binding: PrefDialogTimePickerBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(key: String?): TimePickerPreferenceDialogFragmentCompat {
            val fragment = TimePickerPreferenceDialogFragmentCompat()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        _binding = PrefDialogTimePickerBinding.bind(view)
        val preference: DialogPreference = preference

        if (preference is TimePickerPreference) {
            if (preference.time != null) {
                binding.settingsTimePicker.hour = preference.time!!.hour
                binding.settingsTimePicker.minute = preference.time!!.minute
            } else {
                binding.settingsTimePicker.hour = 22
                binding.settingsTimePicker.minute = 0
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val hour = binding.settingsTimePicker.hour
            val minute = binding.settingsTimePicker.minute

            val result = LocalTime.of(hour, minute)

            if (preference is TimePickerPreference) {
                val timePickerPreference = preference as TimePickerPreference
                if (timePickerPreference.callChangeListener(result)) {
                    timePickerPreference.time = result
                }
            }
        }
    }
}