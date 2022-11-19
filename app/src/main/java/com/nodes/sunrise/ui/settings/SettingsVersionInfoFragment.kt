package com.nodes.sunrise.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nodes.sunrise.BuildConfig
import com.nodes.sunrise.R
import com.nodes.sunrise.databinding.FragmentSettingsVersionInfoBinding
import com.nodes.sunrise.ui.BaseFragment
import java.time.LocalDate

class SettingsVersionInfoFragment : BaseFragment() {

    private var _binding: FragmentSettingsVersionInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsVersionInfoBinding.inflate(inflater)

        setToolbar(binding.fragSettingsVersionInfoTB)
        binding.fragSettingsVersionInfoTB.toolbarTitleSmall.text =
            getString(R.string.frag_settings_version_info_title)
        binding.fragSettingsVersionInfoTB.toolbarSubtitle.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragSettingsVersionInfoTVVersion.text = String.format(
            getString(R.string.frag_settings_version_info_version),
            BuildConfig.VERSION_NAME
        )
        binding.fragSettingsVersionInfoTVCopyright.text = String.format(
            getString(R.string.frag_settings_version_info_copyright),
            if (LocalDate.now().year == 2022) "" else "-${LocalDate.now().year}"
        )
    }
}