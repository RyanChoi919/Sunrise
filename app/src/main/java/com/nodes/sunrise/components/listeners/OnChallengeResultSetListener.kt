package com.nodes.sunrise.components.listeners

import com.nodes.sunrise.enums.ChallengeResult

interface OnChallengeResultSetListener {
    fun onSet(newResult: ChallengeResult)
}
