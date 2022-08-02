package com.nodes.sunrise.ui.challenge.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.ui.BaseFragment
import com.nodes.sunrise.ui.ViewModelFactory

class ChallengeSelectFragment : BaseFragment() {

    companion object {
        fun newInstance() = ChallengeSelectFragment()
    }

    private val viewModel: ChallengeSelectViewModel by viewModels {
        val repository = (requireActivity().application as BaseApplication).repository
        ViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge_select, container, false)
    }

}