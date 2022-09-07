package com.nodes.sunrise.ui.home

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.EntryListAdapter
import com.nodes.sunrise.components.helpers.RecyclerViewHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.FragmentHomeBinding
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeFragment : BaseFragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val entryListAdapter = EntryListAdapter()

    private val viewModel: HomeViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    private val recyclerViewHelper: RecyclerViewHelper<HomeViewModel> by lazy {
        RecyclerViewHelper(this, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        setToolbarBinding(binding.fragHomeTB)

        recyclerViewHelper.setRecyclerViewWithLiveData(
            binding.fragHomeRVRecentEntries,
            entryListAdapter,
            viewModel.allEntries
        )

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        setTextClockFormat()
    }

    override fun onResume() {
        super.onResume()
        val sharedPrefHelper = SharedPreferenceHelper(activity as Activity)
        val savedChallengeId = sharedPrefHelper.getSavedChallengeId()
        if (savedChallengeId != null) {
            val savedDate = sharedPrefHelper.getSavedChallengeDate()!!
            if (savedDate == LocalDate.now()) {
                // challenge가 저장된 날짜가 오늘인 경우
                if (viewModel.currentChallenge.get() != null &&
                    viewModel.currentChallenge.get()!!.challengeId == savedChallengeId
                ) {
                    // viewModel에 저장된 challenge Id와 savedChallengeId가 동일한 경우 do nothing
                } else {
                    // viewModel에 저장된 challenge Id와 savedChallengeId가 동일하지 않은 경우
                    lifecycleScope.launch() {
                        val currentChallenge =
                            viewModel.repository.challengeDao.getChallengeById(savedChallengeId)
                                .first()
                        viewModel.currentChallenge.set(currentChallenge)
                    }
                }
            } else {
                // challenge가 저장된 날짜가 오늘이 아닌 경우, sharedPref와 viewModel의 값을 모두 삭제
                sharedPrefHelper.removeChallengeFromSharedPref()
                viewModel.currentChallenge.set(null)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        // add codes
    }

    private fun setOnClickListeners() {
//        binding.fragHomeIVEditChallenge.setOnClickListener(this)
    }

    private fun setTextClockFormat() {
        val format12Hour =
            (SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat).toLocalizedPattern()
        val format24Hour =
            (SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat).toLocalizedPattern()

        binding.fragHomeTC.format12Hour = format12Hour
        binding.fragHomeTC.format24Hour = format24Hour
    }
}