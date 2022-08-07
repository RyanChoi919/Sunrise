package com.nodes.sunrise.components.comparators

import com.nodes.sunrise.db.entity.Challenge

class ChallengeComparator : BaseComparator<Challenge>() {
    override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge): Boolean {
        return oldItem == newItem
    }
}