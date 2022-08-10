package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.db.entity.ChallengeGroupWithChallenges
import com.nodes.sunrise.model.ChallengesWithGroup

class ChallengesWithGroupComparator : BaseComparator<ChallengesWithGroup>() {

    override fun areContentsTheSame(
        oldItem: ChallengesWithGroup,
        newItem: ChallengesWithGroup
    ): Boolean {
        return oldItem == newItem
    }
}