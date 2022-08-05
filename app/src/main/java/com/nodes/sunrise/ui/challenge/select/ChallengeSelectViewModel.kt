package com.nodes.sunrise.ui.challenge.select

import androidx.databinding.ObservableField
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.ui.BaseViewModel

class ChallengeSelectViewModel(val repository: AppRepository) : BaseViewModel(repository) {
    var currentChallenge: ObservableField<Challenge> = ObservableField(Challenge(0, ""))
}