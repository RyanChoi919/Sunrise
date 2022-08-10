package com.nodes.sunrise.model

import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.ChallengeGroup
import com.nodes.sunrise.enums.ChallengeViewType

data class ChallengesWithGroup(
    val viewType: ChallengeViewType,
    val challengeGroup: ChallengeGroup? = null,
    val challenge: Challenge? = null
)