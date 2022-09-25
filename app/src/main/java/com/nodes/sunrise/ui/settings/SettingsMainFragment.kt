package com.nodes.sunrise.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.nodes.sunrise.R
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.components.preferences.TimePickerPreference
import com.nodes.sunrise.components.preferences.TimePickerPreferenceDialogFragmentCompat
import com.nodes.sunrise.databinding.FragmentSettingsMainBinding
import com.nodes.sunrise.enums.PrefKeys

class SettingsMainFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener,
    Preference.OnPreferenceChangeListener {

    private var _binding: FragmentSettingsMainBinding? = null
    private val binding get() = _binding!!

    private val TAG = this::class.java.simpleName + ".TAG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setDefaultValues()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSettingsMainBinding.bind(view)

        setToolbar()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)

        setOnClickListeners()
        setOnChangeListeners()
    }

    /**
     * 현재 fragment의  Toolbar를 설정하는 function
     * */
    private fun setToolbar() {
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navController = NavHostFragment.findNavController(this)

        NavigationUI.setupWithNavController(
            binding.fragSettingsTB.toolbar, navController, appBarConfiguration
        )

        with(binding.fragSettingsTB) {
            toolbarTitleSmall.text = "설정"
            toolbarTitleLarge.visibility = View.GONE
            toolbarSubtitle.visibility = View.GONE
        }
    }

    private fun setOnClickListeners() {
        val keyList = ArrayList<String>().apply {
            add(PrefKeys.OSS_LICENSES.getKeyString(requireContext()))
            add(PrefKeys.VERSION_INFO.getKeyString(requireContext()))
        }

        val prefList = generatePreferenceListFromPrefKeyList(keyList)

        for (pref in prefList) {
            pref.onPreferenceClickListener = this
        }
    }

    private fun setOnChangeListeners() {
        val keyList = ArrayList<String>().apply {
            add(PrefKeys.NOTIFICATION_DOW.getKeyString(requireContext()))
        }

        val prefList = generatePreferenceListFromPrefKeyList(keyList)

        for (pref in prefList) {
            pref.onPreferenceChangeListener = this
        }
    }

    @Suppress("DEPRECATION")
    override fun onDisplayPreferenceDialog(preference: Preference) {
        var dialogFragment: DialogFragment? = null
        if (preference is TimePickerPreference) {
            dialogFragment = TimePickerPreferenceDialogFragmentCompat.newInstance(preference.key)
        }

        if (dialogFragment != null) {
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, "TAG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }


    /**
     * 알림 요일 선택 시 선택한 요일을 Summary로 표시해주는 extension function
     * @param values : 선택된 알림 요일 값의 Set
     * */
    private fun MultiSelectListPreference.setSummaryFromValues(values: Set<String>) {
        val indexes = values.map { findIndexOfValue(it) }.sorted()

        val indexesWeekday = listOf(0, 1, 2, 3, 4)
        val indexesWeekend = listOf(5, 6)

        summary = if (values.size == 7) {
            resources.getString(R.string.pref_notification_dow_summary_every_day)
        } else if (indexes.size == 5 && indexes.containsAll(indexesWeekday)) {
            resources.getString(R.string.pref_notification_dow_summary_weekday)
        } else if (indexes.size == 2 && indexes.containsAll(indexesWeekend)) {
            resources.getString(R.string.pref_notification_dow_summary_weekend)
        } else {
            entries.filterIndexed { index, _ -> indexes.contains(index) }
                .joinToString(", ")
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        return when (preference.key) {
            PrefKeys.OSS_LICENSES.getKeyString(requireContext()) -> {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.act_oss_licenses_menu_title))
                true
            }
            PrefKeys.VERSION_INFO.getKeyString(requireContext()) -> {
                NavigationHelper(findNavController()).navigateToSettingsVersionInfoFragment()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        return when (preference.key) {
            PrefKeys.NOTIFICATION_DOW.getKeyString(requireContext()) -> {
                val values: ArrayList<String> = ArrayList()

                if (newValue is Set<*>) {
                    for (value in newValue) {
                        if (value is String) {
                            values.add(value)
                        }
                    }
                }

                (preference as MultiSelectListPreference).setSummaryFromValues(values.toSet())
                true
            }
            else -> {
                false
            }
        }
    }

    private fun generatePreferenceListFromPrefKeyList(keyList: ArrayList<String>): ArrayList<Preference> {
        return ArrayList<Preference>().apply {
            for (key in keyList) {
                val pref = findPreference<Preference>(key)
                if (pref != null) {
                    add(pref)
                }
            }
        }
    }

    private fun setDefaultValues() {
        val pref = findPreference<MultiSelectListPreference>(
            PrefKeys.NOTIFICATION_DOW.getKeyString(requireContext())
        )!!
        pref.setSummaryFromValues(pref.values)
    }
}