package com.nodes.sunrise.ui.home

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.EntryListAdapter
import com.nodes.sunrise.components.helpers.RecyclerViewHelper
import com.nodes.sunrise.databinding.FragmentHomeBinding
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import java.util.*

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

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        recyclerViewHelper.setRecyclerViewWithLiveData(
            binding.fragHomeRVRecentEntries,
            entryListAdapter,
            viewModel.allEntries
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        setTextClockFormat()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.fragHomeIVEditChallenge -> {
                findNavController().navigate(R.id.nav_challenge_select)
            }
        }
    }

    private fun setOnClickListeners() {
        binding.fragHomeIVEditChallenge.setOnClickListener(this)
    }

    private fun setTextClockFormat() {
        var formatString =
            (SimpleDateFormat.getDateTimeInstance() as SimpleDateFormat).toLocalizedPattern()
        when (resources.configuration.locales.get(0)) {
            Locale.KOREA -> {
                formatString = "yyyy년 M월 d일 a h:mm:ss"
            }
        }

        binding.fragHomeTC.format12Hour = formatString
    }
}