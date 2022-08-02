package com.nodes.sunrise.ui.challenge.select

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nodes.sunrise.BaseApplication
import com.nodes.sunrise.R
import com.nodes.sunrise.ui.ViewModelFactory
import com.nodes.sunrise.ui.home.HomeViewModel

class ChallengeSelectFragment : Fragment() {

    companion object {
        fun newInstance() = ChallengeSelectFragment()
    }

    private val viewModel: HomeViewModel by viewModels {
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