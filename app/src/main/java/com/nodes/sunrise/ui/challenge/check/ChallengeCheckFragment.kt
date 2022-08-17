package com.nodes.sunrise.ui.challenge.check

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
import com.nodes.sunrise.components.helpers.NavigationHelper
import com.nodes.sunrise.components.helpers.SharedPreferenceHelper
import com.nodes.sunrise.databinding.FragmentChallengeCheckBinding
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.enums.ChallengeResult
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ChallengeCheckFragment : BaseFragment(), View.OnClickListener {

    companion object {
        val KEY_CHALLENGE = this::class.java.simpleName + ".CHALLENGE"
    }

    private var _binding: FragmentChallengeCheckBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChallengeCheckViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_challenge_check, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        val challenge = requireArguments().getSerializable(KEY_CHALLENGE) as Challenge?
        if (challenge != null) {
            viewModel.currentChallenge.set(challenge)
        } else {
            val sharedPrefHelper = SharedPreferenceHelper(activity as Activity)
            val savedChallengeId = sharedPrefHelper.getSavedChallengeId()
            if (savedChallengeId != null) {
                lifecycleScope.launch {
                    val savedChallenge =
                        viewModel.repository.challengeDao.getChallengeById(savedChallengeId).first()
                    viewModel.currentChallenge.set(savedChallenge)
                }
            }
        }

        /* data binding 변수 설정 */
        binding.viewModel = viewModel

        setOnClickListeners()

        return binding.root
    }

    override fun onClick(p0: View?) {
        with(binding) {
            when (p0) {
                fragChallengeCheckIVSuccessButton -> {
                    viewModel!!.challengeResult = ChallengeResult.SUCCESS
                    toggleButtons()
                }
                fragChallengeCheckIVFailButton -> {
                    viewModel!!.challengeResult = ChallengeResult.FAIL
                    toggleButtons()
                }
                fragChallengeCheckIVPostponeButton -> {
                    viewModel!!.challengeResult = ChallengeResult.POSTPONE
                    toggleButtons()
                }
                fragChallengeCheckBTNWriteEntry -> {
                    NavigationHelper(findNavController()).navigateToEntryWriteFragmentToCreate()
                }
            }
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            fragChallengeCheckIVSuccessButton.setOnClickListener(this@ChallengeCheckFragment)
            fragChallengeCheckIVFailButton.setOnClickListener(this@ChallengeCheckFragment)
            fragChallengeCheckIVPostponeButton.setOnClickListener(this@ChallengeCheckFragment)
            fragChallengeCheckBTNWriteEntry.setOnClickListener(this@ChallengeCheckFragment)

            fragChallengeCheckCBTryAgainTomorrow.setOnCheckedChangeListener { _, isChecked ->
                viewModel!!.shouldTryAgain = isChecked
            }
        }
    }

    private fun toggleButtons() {
        with(binding) {
            when (viewModel!!.challengeResult) {
                ChallengeResult.SUCCESS -> {
                    fragChallengeCheckIVSuccessButton.isSelected = true
                    fragChallengeCheckTVSuccessButtonLabel.visibility = View.VISIBLE

                    fragChallengeCheckIVFailButton.isSelected = false
                    fragChallengeCheckTVFailButtonLabel.visibility = View.GONE

                    fragChallengeCheckIVPostponeButton.isSelected = false
                    fragChallengeCheckTVPostponeButtonLabel.visibility = View.GONE

                    fragChallengeCheckCBTryAgainTomorrow.visibility = View.GONE
                }
                ChallengeResult.FAIL -> {
                    fragChallengeCheckIVSuccessButton.isSelected = false
                    fragChallengeCheckTVSuccessButtonLabel.visibility = View.GONE

                    fragChallengeCheckIVFailButton.isSelected = true
                    fragChallengeCheckTVFailButtonLabel.visibility = View.VISIBLE

                    fragChallengeCheckIVPostponeButton.isSelected = false
                    fragChallengeCheckTVPostponeButtonLabel.visibility = View.GONE

                    fragChallengeCheckCBTryAgainTomorrow.visibility = View.VISIBLE
                }
                ChallengeResult.POSTPONE -> {
                    fragChallengeCheckIVSuccessButton.isSelected = false
                    fragChallengeCheckTVSuccessButtonLabel.visibility = View.GONE

                    fragChallengeCheckIVFailButton.isSelected = false
                    fragChallengeCheckTVFailButtonLabel.visibility = View.GONE

                    fragChallengeCheckIVPostponeButton.isSelected = true
                    fragChallengeCheckTVPostponeButtonLabel.visibility = View.VISIBLE

                    fragChallengeCheckCBTryAgainTomorrow.visibility = View.GONE
                }
            }
        }
    }
}