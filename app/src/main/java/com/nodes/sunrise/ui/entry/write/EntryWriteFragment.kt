package com.nodes.sunrise.ui.entry.write

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnSuccessListener
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.PhotoPreviewListAdapter
import com.nodes.sunrise.components.helpers.RecyclerViewHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.components.utils.AlertDialogUtil
import com.nodes.sunrise.components.utils.LocationUtil
import com.nodes.sunrise.databinding.FragmentEntryWriteBinding
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import gun0912.tedimagepicker.builder.TedImagePicker
import java.time.LocalDate
import java.time.LocalTime

class EntryWriteFragment() : BaseFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener {

    companion object {
        val KEY_ENTRY = this::class.java.simpleName + ".ENTRY"
        private const val TAG = "EntryWriteFragment.TAG"
    }

    private var _binding: FragmentEntryWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntryWriteViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }
    private val photoPreviewListAdapter =
        PhotoPreviewListAdapter(object : PhotoPreviewListAdapter.OnPhotoPreviewListEmptyListener {
            override fun onListEmpty() {
                binding.fragEntryWriteRVPictures.visibility = View.GONE
                binding.fragEntryWriteCBEntryPicture.isChecked = false
            }
        })
    private val recyclerViewHelper: RecyclerViewHelper<EntryWriteViewModel> by lazy {
        RecyclerViewHelper(this, viewModel)
    }
    private val onSuccessListener = OnSuccessListener<Location> {
        viewModel.updateEntryLocation(it)
        binding.fragEntryWriteCBEntryPlace.isChecked = true
//        updateCurrentWeather()
    }
    private var isToCreateMode = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /* handle on back pressed */
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isEntryModified()) {
                    AlertDialogUtil.showEntryNotSavedAlertDialog(this@EntryWriteFragment)
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry_write, container, false)

        /* data binding 설정 */
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        /* Toolbar binding 설정 */
        setToolbar(binding.fragEntryWriteTB)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Listeners 설정 */
        setOnClickListeners()
        setOnCheckedChangedListeners()

        /* 콘텐츠 EditText의 글자 수를 카운트&표시하기 위한 TextWatcher 설정 */
        binding.fragEntryWriteETContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                val textCountString = if (p0 != null && p0.toString() != "") {
                    p0.toString().replace(" ", "").length.toString()
                } else {
                    "0"
                }
                viewModel.textCount.set(textCountString)
            }
        })

        /* 선택된 사진의 thumbnail을 보여주는 recycler view 초기화 */
        recyclerViewHelper.setRecyclerViewWithPictureUri(
            binding.fragEntryWriteRVPictures, photoPreviewListAdapter, emptyList()
        )
        photoPreviewListAdapter.submitList(viewModel.currentEntry.get()!!.photos)

        /* argument 확인 및 viewModel 데이터 초기화 */
        if (savedInstanceState == null && arguments != null) {
            val entry = requireArguments().getSerializable(KEY_ENTRY) as Entry?

            if (entry != null) {
                isToCreateMode = false
                viewModel.currentEntry.set(entry.copy())
                viewModel.prevEntry = entry.copy()
                viewModel.isPrevEntrySet = true

                // 제목 활성화 여부 확인 및 적용
                binding.fragEntryWriteCBTitle.isChecked = entry.isTitleEnabled
                // 위치 활성화 여부 확인 및 적용
                binding.fragEntryWriteCBEntryPlace.isChecked =
                    entry.latitude != null && entry.longitude != null
            } else {
                isToCreateMode = true

                /* Preference 기본값 설정 확인 */
                val pref = SharedPreferenceHelper(requireContext())
                // 제목 자동 활성화 여부 확인 및 적용
                binding.fragEntryWriteCBTitle.isChecked = pref.getSavedShouldEnableTitleByDefault()
                // 위치 자동 추가 여부 확인 및 적용
                binding.fragEntryWriteCBEntryPlace.isChecked =
                    pref.getSavedShouldAddLocationByDefault()
            }
        }

        /* 툴바 및 메뉴 설정 */
        setToolbarTitleWithDateTime(viewModel.currentEntry.get()!!.dateTime)

        val menuRes = if (isToCreateMode) {
            R.menu.frag_entry_write_menu_create
        } else {
            R.menu.frag_entry_write_menu_modify
        }

        createMenu(binding.fragEntryWriteTB.toolbar, menuRes) { menuItem ->
            when (menuItem.itemId) {
                R.id.frag_entry_write_menu_save -> {
                    viewModel.saveEntry()
                    findNavController().popBackStack()
                    true
                }
                R.id.frag_entry_write_menu_modify -> {
                    viewModel.modifyEntry()
                    findNavController().popBackStack()
                    true
                }
                R.id.frag_entry_write_menu_delete -> {
                    AlertDialogUtil.showEntryDeleteConfirmDialog(
                        this@EntryWriteFragment, viewModel, viewModel.currentEntry.get()!!
                    )
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        Log.d(TAG, "onClick: $v")
        with(binding) {
            when (v) {
                fragEntryWriteCBEntryDate -> {
                    val prevDateTime = viewModel!!.currentEntry.get()!!.dateTime
                    DatePickerDialog(
                        requireContext(), { _, y, m, d ->
                            val newDate = LocalDate.of(y, m + 1, d)

                            /* date가 새로운 값으로 업데이트 된 경우 */
                            if (newDate != prevDateTime.toLocalDate()) {
                                viewModel!!.setEntryDate(newDate)
                                setToolbarTitleWithDateTime(viewModel!!.currentEntry.get()!!.dateTime)
                            }
                        },
                        prevDateTime.year,
                        prevDateTime.monthValue - 1, // DatePicker index는 0부터 시작
                        prevDateTime.dayOfMonth
                    ).show()

                    fragEntryWriteCBEntryDate.isChecked = true // always checked
                }
                fragEntryWriteCBEntryTime -> {
                    val prevDateTime = viewModel!!.currentEntry.get()!!.dateTime
                    TimePickerDialog(context, { _, h, m ->
                        val newTime = LocalTime.of(h, m)

                        /* time이 새로운 값으로 업데이트 된 경우 */
                        if (newTime != prevDateTime.toLocalTime()) {
                            viewModel!!.setEntryTime(newTime)
                            setToolbarTitleWithDateTime(viewModel!!.currentEntry.get()!!.dateTime)
                        }
                    }, prevDateTime.hour, prevDateTime.minute, false).show()

                    fragEntryWriteCBEntryTime.isChecked = true  // always checked
                }
                fragEntryWriteCBEntryPicture -> {
                    TedImagePicker.with(requireContext())
                        .selectedUri(photoPreviewListAdapter.currentList)
                        .startMultiImage { uriList ->
                            fragEntryWriteCBEntryPicture.isChecked = uriList.isNotEmpty()
                            fragEntryWriteRVPictures.visibility =
                                if (uriList.isNotEmpty()) View.VISIBLE else View.GONE
                            photoPreviewListAdapter.submitList(uriList)
                            viewModel!!.setPhotoUriList(uriList)
                        }

                    fragEntryWriteCBEntryPicture.isChecked =
                        viewModel!!.currentEntry.get()!!.photos.isNotEmpty()
                }
            }
        }
    }

    override fun onCheckedChanged(v: CompoundButton?, isChecked: Boolean) {
        with(binding) {
            when (v) {
                fragEntryWriteCBTitle -> {
                    viewModel!!.setEntryTitleEnabled(isChecked)
                    fragEntryWriteETTitle.visibility = if (isChecked) View.VISIBLE else View.GONE
                }
                fragEntryWriteCBEntryPlace -> {
                    if (isChecked) {
                        val currentEntry = viewModel!!.currentEntry.get()!!
                        val doesEntryHaveLocation = currentEntry.run {
                            latitude != null && longitude != null
                        }

                        /* entry에 이미 입력된 location 정보가 없는 경우에만 location을 업데이트함.
                        (기존 존재하는 entry를 수정하기 위해 EntryWriteFragment를 실행하는 경우,
                        이미 기존 location이 있는데 CB가 checked 됨에 따라 새로운 Location을 받아오는 것을
                        방지하기 위함.) */
                        if (doesEntryHaveLocation) {
                            // do nothing
                        } else {
                            LocationUtil.getCurrentLocation(requireActivity(), onSuccessListener)
                        }
                    } else {
                        viewModel!!.removeEntryLocation()
                    }
                }
                else -> {
                    // do nothing
                }
            }
        }

        Log.d(TAG, "onCheckedChanged: currentEntry = ${viewModel.currentEntry.get()!!}")
    }

    private fun setOnClickListeners() {
        val views = ArrayList<View>().apply {
            with(binding) {
                add(fragEntryWriteCBEntryDate)
                add(fragEntryWriteCBEntryTime)
                add(fragEntryWriteCBEntryPicture)
            }
        }

        for (v in views) {
            v.setOnClickListener(this@EntryWriteFragment)
        }
    }

    private fun setOnCheckedChangedListeners() {
        val views = ArrayList<CheckBox>().apply {
            with(binding) {
                add(fragEntryWriteCBTitle)
                add(fragEntryWriteCBEntryPlace)
            }
        }

        for (v in views) {
            v.setOnCheckedChangeListener(this)
        }
    }

//    private fun updateCurrentWeather() {
//        val currentEntry = viewModel.currentEntry.get()!!
//        with(currentEntry) {
//            if (latitude != null && longitude != null) {
//                WeatherUtil.getCurrentWeather(
//                    latitude!!, longitude!!,
//                    object : Callback<WeatherInfo> {
//                        override fun onResponse(
//                            call: Call<WeatherInfo>,
//                            response: Response<WeatherInfo>
//                        ) {
//                            weatherInfo = response.body()
//                            Log.d(TAG, "onResponse: $weatherInfo")
//                        }
//
//                        override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
//                            Toast.makeText(
//                                requireContext(),
//                                "날씨 정보 확인에 실패했습니다.",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            t.printStackTrace()
//                        }
//                    })
//            }
//        }
//    }
}