package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.model.ChallengeAndGroup

class ChallengesWithGroupComparator : BaseComparator<ChallengeAndGroup>() {

    override fun areContentsTheSame(
        oldItem: ChallengeAndGroup,
        newItem: ChallengeAndGroup
    ): Boolean {
        return oldItem == newItem
    }
}