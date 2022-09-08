package com.nodes.sunrise.ui.entry.write

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.PhotoPreviewListAdapter
import com.nodes.sunrise.components.helpers.RecyclerViewHelper
import com.nodes.sunrise.components.listeners.OnPermissionRationaleResultListener
import com.nodes.sunrise.components.utils.AlertDialogUtil
import com.nodes.sunrise.components.utils.LocationUtil
import com.nodes.sunrise.databinding.FragmentEntryWriteBinding
import com.nodes.sunrise.db.entity.Entry
import com.nodes.sunrise.enums.Permission
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import gun0912.tedimagepicker.builder.TedImagePicker
import java.time.LocalDateTime

class EntryWriteFragment() : BaseFragment(), View.OnClickListener {

    companion object {
        val KEY_ENTRY = this::class.java.simpleName + ".ENTRY"
    }

    private var _binding: FragmentEntryWriteBinding? = null
    private val binding get() = _binding!!

    private var isToCreateMode = true

    private val viewModel: EntryWriteViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    private val locationManager by lazy {
        requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private val adapter = PhotoPreviewListAdapter()

    private val recyclerViewHelper: RecyclerViewHelper<EntryWriteViewModel> by lazy {
        RecyclerViewHelper(this, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_entry_write, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setToolbarBinding(binding.fragEntryWriteTB)

        setOnClickListeners()

        /* arguments 확인 및 viewModel 데이터 초기화 */
        if (savedInstanceState == null && arguments != null) {
            val entry = requireArguments().getSerializable(KEY_ENTRY) as Entry?

            if (entry != null) {
                isToCreateMode = false
                viewModel.currentEntry.set(entry.copy())
                viewModel.prevEntry = entry.copy()
                viewModel.isPrevEntrySet = true
            } else {
                isToCreateMode = true
                updateCurrentLocation()
            }
            checkTitleEnabled()
            setToolbarTitleWithDateTime(viewModel.currentEntry.get()!!.dateTime)
        }

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

        recyclerViewHelper.setRecyclerViewWithPictureUri(
            binding.fragEntryWriteRVPictures, adapter, emptyList()
        )

        adapter.submitList(viewModel.currentEntry.get()!!.photos)

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    private fun setOnClickListeners() {
        val views = ArrayList<View>().apply {
            with(binding) {
                add(fragEntryWriteMCBEntryDate)
                add(fragEntryWriteMCBEntryTime)
                add(fragEntryWriteMCBTitle)
                add(fragEntryWriteMCBEntryPlace)
                add(fragEntryWriteMCBEntryPicture)
            }
        }

        for (v in views) {
            v.setOnClickListener(this@EntryWriteFragment)
        }
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v) {
                fragEntryWriteMCBEntryDate -> {
                    val currentEntry = viewModel!!.currentEntry.get()!!
                    DatePickerDialog(
                        requireContext(), { _, y, m, d ->
                            val newDateTime =
                                LocalDateTime.from(currentEntry.dateTime).withYear(y)
                                    .withMonth(m + 1)
                                    .withDayOfMonth(d)
                            currentEntry.dateTime = newDateTime
                            setToolbarTitleWithDateTime(newDateTime)
                            viewModel!!.currentEntry.set(currentEntry)
                        },
                        currentEntry.dateTime.year,
                        currentEntry.dateTime.monthValue - 1, // DatePicker index는 0부터 시작
                        currentEntry.dateTime.dayOfMonth
                    ).show()
                    fragEntryWriteMCBEntryDate.isChecked = true
                }
                fragEntryWriteMCBEntryTime -> {
                    val currentEntry = viewModel!!.currentEntry.get()!!
                    TimePickerDialog(context, { _, h, m ->
                        val newDateTime =
                            LocalDateTime.from(currentEntry.dateTime).withHour(h).withMinute(m)
                        currentEntry.dateTime = newDateTime
                        setToolbarTitleWithDateTime(newDateTime)
                        viewModel!!.currentEntry.set(currentEntry)
                    }, currentEntry.dateTime.hour, currentEntry.dateTime.minute, false).show()
                    fragEntryWriteMCBEntryTime.isChecked = true
                    setToolbarTitleWithDateTime(currentEntry.dateTime)
                }
                fragEntryWriteMCBTitle -> {
                    val toastMessage: String
                    val currentEntry = viewModel!!.currentEntry.get()!!
                    currentEntry.isTitleEnabled = !currentEntry.isTitleEnabled

                    if (currentEntry.isTitleEnabled) {
                        fragEntryWriteETTitle.visibility = View.GONE
                        toastMessage = getString(R.string.toast_message_title_disabled)
                    } else {
                        fragEntryWriteETTitle.visibility = View.VISIBLE
                        toastMessage = getString(R.string.toast_message_title_enabled)
                    }
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()

                    viewModel!!.currentEntry.set(currentEntry)
                }
                fragEntryWriteMCBEntryPlace -> {
                    if (!fragEntryWriteMCBEntryPlace.isChecked) {
                        viewModel!!.removeEntryLocation()
                        fragEntryWriteMCBEntryPlace.isChecked = false
                    } else {
                        updateCurrentLocation()
                        fragEntryWriteMCBEntryPlace.isChecked = true
                    }
                }
                fragEntryWriteMCBEntryPicture -> {
                    TedImagePicker.with(requireContext())
                        .selectedUri(adapter.currentList)
                        .startMultiImage { uriList ->
                            if (uriList.isEmpty()) {
                                fragEntryWriteRVPictures.visibility = View.GONE
                            } else {
                                fragEntryWriteRVPictures.visibility = View.VISIBLE
                            }
                            adapter.submitList(uriList)
                            val currentEntry = viewModel!!.currentEntry.get()!!
                            currentEntry.photos = uriList
                            viewModel!!.currentEntry.set(currentEntry)
                        }
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {

        val hasFineLocationPermission = hasPermission(Permission.FINE_LOCATION.androidName)
        val hasCoarseLocationPermission = hasPermission(Permission.COARSE_LOCATION.androidName)

        if (hasFineLocationPermission && hasCoarseLocationPermission) {
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object :
                CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                    return CancellationTokenSource().token
                }

                override fun isCancellationRequested(): Boolean {
                    return false
                }
            }).addOnSuccessListener {
                Log.d(TAG, "updateCurrentLocation: getCurrentLocation = $it")
                viewModel.updateEntryLocation(it)
            }
        } else {
            requestLocationPermissions()
        }
    }

    private fun hasPermission(androidPermission: String): Boolean {
        val result = ActivityCompat.checkSelfPermission(
            requireContext(), androidPermission
        ) == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "hasPermission: $androidPermission = $result")
        return result
    }

    private fun requestLocationPermissions() {
        if (shouldShowRequestPermissionRationale(Permission.COARSE_LOCATION.androidName) ||
            shouldShowRequestPermissionRationale(Permission.FINE_LOCATION.androidName)
        ) {
            AlertDialogUtil.showLocationPermissionRationaleDialog(
                requireContext(),
                object : OnPermissionRationaleResultListener {
                    override fun onResultSet(isPositive: Boolean) {
                        when {
                            isPositive -> {
                                ActivityCompat.requestPermissions(
                                    requireActivity(), arrayOf(
                                        Permission.COARSE_LOCATION.androidName,
                                        Permission.FINE_LOCATION.androidName
                                    ), this::class.java.hashCode()
                                )
                            }
                            else -> {
                                // do nothing (permission not granted)
                            }
                        }
                    }
                })
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Permission.COARSE_LOCATION.androidName,
                    Permission.FINE_LOCATION.androidName
                ), this::class.java.hashCode()
            )
        }
    }

    private fun checkTitleEnabled() {
        with(binding) {
            val entry = this@EntryWriteFragment.viewModel.currentEntry.get()!!
            if (entry.isTitleEnabled) {
                fragEntryWriteETTitle.visibility = View.VISIBLE
                fragEntryWriteMCBTitle.isChecked = true
            } else {
                fragEntryWriteETTitle.visibility = View.GONE
                fragEntryWriteMCBTitle.isChecked = false
            }
        }
    }
}