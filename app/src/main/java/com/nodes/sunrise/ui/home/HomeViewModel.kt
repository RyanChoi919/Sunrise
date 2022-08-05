package com.nodes.sunrise.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.ui.BaseViewModel

class HomeViewModel(val repository: AppRepository) : BaseViewModel(repository) {
    val currentChallenge = ObservableField(Challenge(0, ""))
}