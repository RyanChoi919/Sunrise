package com.nodes.sunrise.ui.challenge.check

import androidx.databinding.ObservableField
import com.nodes.sunrise.db.AppRepository
import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.enums.ChallengeResult
import com.nodes.sunrise.ui.BaseViewModel

class ChallengeCheckViewModel(val repository: AppRepository) : BaseViewModel(repository) {
    var currentChallenge: ObservableField<Challenge> = ObservableField(Challenge(0, ""))
    var challengeResult: ChallengeResult = ChallengeResult.POSTPONE
    var shouldTryAgain: Boolean = false
}