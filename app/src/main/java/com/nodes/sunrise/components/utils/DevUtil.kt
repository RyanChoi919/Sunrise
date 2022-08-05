package com.nodes.sunrise.components.utils

import com.nodes.sunrise.db.entity.Challenge

class DevUtil {
    companion object {
        fun createSampleChallenges(count: Int): ArrayList<Challenge> {
            val result = ArrayList<Challenge>()
            for (i in 1..count) {
                result.add(Challenge(0, String.format("도전 과제 %d", i)))
            }

            return result
        }
    }
}