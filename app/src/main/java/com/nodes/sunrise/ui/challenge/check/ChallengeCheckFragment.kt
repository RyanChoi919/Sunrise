package com.nodes.sunrise.ui.challenge.check

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory
import com.nodes.sunrise.ui.challenge.select.ChallengeSelectViewModel

class ChallengeCheckFragment : BaseFragment() {

    companion object {
        fun newInstance() = ChallengeCheckFragment()
    }

    private val viewModel: ChallengeCheckViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge_check, container, false)
    }

}