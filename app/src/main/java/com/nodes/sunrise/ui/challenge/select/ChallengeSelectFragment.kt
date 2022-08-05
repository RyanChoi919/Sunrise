package com.nodes.sunrise.ui.challenge.select

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.FragmentChallengeSelectBinding
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChallengeSelectFragment : BaseFragment(), View.OnClickListener {

    companion object {
        val KEY_CHALLENGE = this::class.java.simpleName + ".CHALLENGE"
    }

    private var _binding: FragmentChallengeSelectBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeSelectViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_select, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        val challenge = requireArguments().getSerializable(KEY_CHALLENGE) as Challenge?
        if (challenge != null) {
            viewModel.currentChallenge.set(challenge)
        } else {
            val sharedPrefHelper = SharedPreferenceHelper(activity as Activity)
            val savedChallengeId = sharedPrefHelper.getSavedChallengeId()
            if (savedChallengeId != null) {
                lifecycleScope.launch() {
                    with(viewModel) {
                        currentChallenge.set(
                            repository.challengeDao.getEntityById(savedChallengeId).first()
                        )
                    }
                }
            } else {
                refreshChallenge()
            }
        }

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.fragChallengeSelectIBRefreshButton -> refreshChallenge()
            binding.fragChallengeSelectBTNAcceptChallenge -> {
                val sharedPreferenceHelper = SharedPreferenceHelper(activity as Activity)
                sharedPreferenceHelper.saveChallengeToSharedPref(viewModel.currentChallenge.get()!!)
                findNavController().popBackStack()
            }
        }
    }

    private fun setOnClickListener() {
        with(binding) {
            fragChallengeSelectIBRefreshButton.setOnClickListener(this@ChallengeSelectFragment)
            fragChallengeSelectBTNAcceptChallenge.setOnClickListener(this@ChallengeSelectFragment)
        }
    }

    private fun refreshChallenge() {
        lifecycleScope.launch {
            val allChallenges = viewModel.repository.allChallenges.first()
            viewModel.currentChallenge.set(allChallenges[(allChallenges.indices).random()])
        }
    }

}