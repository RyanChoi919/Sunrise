package com.nodes.sunrise.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.ui.challenge.check.ChallengeCheckViewModel
import com.nodes.sunrise.ui.challenge.select.ChallengeSelectViewModel
import com.nodes.sunrise.ui.home.HomeViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChallengeSelectViewModel::class.java) -> {
                return ChallengeSelectViewModel(repository) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
    }
}