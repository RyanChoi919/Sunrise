package com.nodes.sunrise.ui.entry.read

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.components.utils.LocationUtil
import com.nodes.sunrise.databinding.FragmentEntryReadBinding
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.launch


class EntryReadFragment : BaseFragment(), View.OnClickListener {

    companion object {
        val KEY_ENTRY = this::class.java.simpleName + ".ENTRY"
    }

    private var _binding: FragmentEntryReadBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntryReadViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry_read, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setToolbarBinding(binding.fragEntryReadTB)

        /* bundle 에서 Entry 받아와서 viewModel 내 Entry 초기화 */
        viewModel.currentEntry =
            requireArguments().getSerializable(KEY_ENTRY) as Entry
        setLocationText()
        setToolbarTitleWithDateTime(viewModel.currentEntry.dateTime)

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createMenu(binding.fragEntryReadTB.toolbar, R.menu.frag_entry_read_menu) { menuItem ->
            when (menuItem.itemId) {
                R.id.frag_entry_write_menu_edit -> {
                    NavigationHelper(findNavController()).navigateToEntryWriteFragmentToModify(
                        viewModel.currentEntry
                    )
                    true
                }
                else -> false
            }
        }
        setOnClickListeners()
        setPictureView()
//        setWeatherInfo()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.frag_entry_read_FL_picture -> {
                NavigationHelper(findNavController()).navigateToPhotoFragment(viewModel.currentEntry.photos)
            }
        }
    }

    private fun setOnClickListeners() {
        val views = ArrayList<View>().apply {
            with(binding) {
                add(fragEntryReadFLPicture)
            }
        }

        for (v in views) {
            v.setOnClickListener(this@EntryReadFragment)
        }
    }

    private fun setLocationText() {
        with(binding.fragEntryReadTVLocation) {
            val latitude = viewModel.currentEntry.latitude
            val longitude = viewModel.currentEntry.longitude

            if (latitude != null && longitude != null) {
                lifecycleScope.launch {
                    text = LocationUtil.getAddressFromLatLong(requireContext(), latitude, longitude)
                        .getAddressLine(0)
                }
            } else {
                text = getString(R.string.no_location_information)
            }
        }
    }

    private fun setPictureView() {
        with(binding) {
            if (viewModel!!.currentEntry.photos.isNotEmpty()) {
                fragEntryReadFLPicture.visibility = View.VISIBLE
                Glide.with(requireContext()).load(viewModel!!.currentEntry.photos[0])
                    .into(fragEntryReadIVPicture)
                fragEntryReadIVMultiplePhotosIcon.visibility =
                    if (viewModel!!.currentEntry.photos.size > 1) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
            } else {
                fragEntryReadFLPicture.visibility = View.GONE
            }
        }
    }

//    private fun setWeatherInfo() {
//        val currentEntry = viewModel.currentEntry
//        with(binding.fragEntryReadTVWeatherInfo) {
//            if (currentEntry.weatherInfo != null) {
//
//                val iconUrl =
//                    "https://openweathermap.org/img/wn/${currentEntry.weatherInfo!!.weather[0].icon}@2x.png"
//                Log.d(TAG, "setWeatherInfo: iconUrl = $iconUrl")
//
//                Glide.with(requireContext())
//                    .load(iconUrl)
//                    .into(object : CustomTarget<Drawable>() {
//                        override fun onResourceReady(
//                            resource: Drawable,
//                            transition: Transition<in Drawable>?
//                        ) {
//                            text = WeatherUtil.fromKelvinToLocaleUnit(
//                                currentEntry.weatherInfo!!.main!!.temp,
//                                Locale.getDefault()
//                            )
//                            setCompoundDrawablesWithIntrinsicBounds(resource, null, null, null)
//                        }
//
//                        override fun onLoadCleared(placeholder: Drawable?) {
//                            text = "날씨 정보 없음"
//                            setCompoundDrawablesWithIntrinsicBounds(
//                                R.drawable.ic_round_cloud_off_24,
//                                0,
//                                0,
//                                0
//                            )
//                        }
//
//                    })
//            } else {
//                text = "날씨 정보 없음"
//                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_round_cloud_off_24, 0, 0, 0)
//            }
//        }
//    }
}