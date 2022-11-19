package com.nodes.sunrise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.nodes.sunrise.R
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.FragmentSettingsFontBinding
import com.nodes.sunrise.ui.BaseFragment

class SettingsFontFragment : BaseFragment(), RadioGroup.OnCheckedChangeListener {

    private var _binding: FragmentSettingsFontBinding? = null
    val binding: FragmentSettingsFontBinding
        get() = _binding!!
    private lateinit var prefHelper: SharedPreferenceHelper
    private var savedFont: Int = 0
    private var newFont: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsFontBinding.inflate(inflater)

        prefHelper = SharedPreferenceHelper(requireContext())
        checkPrevFontRadioButton()
        savedFont = prefHelper.getSavedFont()
        newFont = prefHelper.getSavedFont()

        setToolbar(binding.fragSettingsFontTB)
        with(binding.fragSettingsFontTB) {
            toolbarTitleSmall.text = getString(R.string.frag_settings_font_title)
            toolbarSubtitle.visibility = View.GONE
        }

        binding.fragSettingsFontRG.setOnCheckedChangeListener(this)

        return binding.root
    }

    private fun checkPrevFontRadioButton() {
        binding.fragSettingsFontRG.check(
            when (prefHelper.getSavedFont()) {
                R.font.nanum_myeongjo -> R.id.frag_settings_font_RB_nanum_myeongjo
                R.font.nanum_gothic -> R.id.frag_settings_font_RB_nanum_gothic
                R.font.gamja_flower -> R.id.frag_settings_font_RB_gamja_flower
                R.font.single_day -> R.id.frag_settings_font_RB_single_day
                else -> R.id.frag_settings_font_RB_nanum_myeongjo
            }
        )
    }

    override fun onCheckedChanged(radioGroup: RadioGroup, index: Int) {
        newFont = when (radioGroup.checkedRadioButtonId) {
            R.id.frag_settings_font_RB_nanum_myeongjo -> {
                R.font.nanum_myeongjo
            }
            R.id.frag_settings_font_RB_nanum_gothic -> {
                R.font.nanum_gothic
            }
            R.id.frag_settings_font_RB_gamja_flower -> {
                R.font.gamja_flower
            }
            R.id.frag_settings_font_RB_single_day -> {
                R.font.single_day
            }
            else -> R.font.nanum_myeongjo // default value
        }
        prefHelper.saveFont(newFont)

        if (savedFont != newFont) {
            requireActivity().recreate()
        }
    }
}