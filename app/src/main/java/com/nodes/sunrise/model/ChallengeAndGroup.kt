package com.nodes.sunrise.model

import com.nodes.sunrise.db.entity.Challenge
import com.nodes.sunrise.db.entity.ChallengeGroup
import com.nodes.sunrise.enums.EntryViewType

data class ChallengeAndGroup(
    val viewType: EntryViewType,
    val challengeGroup: ChallengeGroup? = null,
    val challenge: Challenge? = null
)