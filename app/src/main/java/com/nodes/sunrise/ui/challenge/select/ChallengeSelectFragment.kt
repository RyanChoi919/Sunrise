package com.nodes.sunrise.ui.challenge.select

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.adapters.list.ChallengeListAdapter
import com.nodes.sunrise.components.helpers.RecyclerViewHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.components.listeners.OnEntityClickListener
import com.nodes.sunrise.databinding.FragmentChallengeSelectBinding
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChallengeSelectFragment : Fragment(), View.OnClickListener {

    companion object {
        val KEY_CHALLENGE = this::class.java.simpleName + ".CHALLENGE"
    }

    private var _binding: FragmentChallengeSelectBinding? = null
    private val binding get() = _binding!!
    private val challengeListAdapter = ChallengeListAdapter()

    private val viewModel: ChallengeSelectViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    private val recyclerViewHelper: RecyclerViewHelper<ChallengeSelectViewModel> by lazy {
        RecyclerViewHelper(this, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_select, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        recyclerViewHelper.setRecyclerViewWithLiveData(
            binding.fragChallengeSelectRVChallengeList,
            challengeListAdapter,
            viewModel.allChallengeGroupsWithChallenges,
        )

        val challenge = requireArguments().getSerializable(KEY_CHALLENGE) as Challenge?
        if (challenge != null) {
            challengeListAdapter.selectedChallenge = challenge
        } else {
            val sharedPrefHelper = SharedPreferenceHelper(activity as Activity)
            val savedChallengeId = sharedPrefHelper.getSavedChallengeId()
            if (savedChallengeId != null) {
                lifecycleScope.launch {
                    challengeListAdapter.selectedChallenge =
                        viewModel.repository.challengeDao.getChallengeById(savedChallengeId).first()
                }
            }
        }

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        setRecyclerView()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.fragChallengeSelectBTNAcceptChallenge -> {
                val sharedPreferenceHelper = SharedPreferenceHelper(activity as Activity)
                sharedPreferenceHelper.saveChallengeToSharedPref(challengeListAdapter.selectedChallenge)
                findNavController().popBackStack()
            }
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            fragChallengeSelectBTNAcceptChallenge.setOnClickListener(this@ChallengeSelectFragment)
        }
    }

    private fun setRecyclerView() {
        with(binding.fragChallengeSelectRVChallengeList) {
            // recyclerView 아이템 선택 시 blink 하는 것을 방지
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            // recyclerView 아이템 클릭 시 button 활성화
            challengeListAdapter.onClickListener = object : OnEntityClickListener<Challenge> {
                override fun onClick() {
                    binding.fragChallengeSelectBTNAcceptChallenge.isEnabled = true
                }
            }
        }

    }
}