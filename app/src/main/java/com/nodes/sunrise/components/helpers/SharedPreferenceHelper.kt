package com.nodes.sunrise.components.helpers

import android.app.Activity
import android.content.Context
import com.nodes.sunrise.R
import com.nodes.sunrise.db.entity.Challenge
import java.time.LocalDate

class SharedPreferenceHelper(activity: Activity) {

    private val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    private val challengeIdKey: String =
        activity.resources.getString(R.string.pref_key_challenge_id)
    private val challengeDateKey: String =
        activity.resources.getString(R.string.pref_key_challenge_date)

    fun getSavedChallengeId(): Int? {
        val id = sharedPref.getInt(challengeIdKey, -1)
        return if (id == -1) null else id
    }

    fun getSavedChallengeDate(): LocalDate? {
        val epochDay = sharedPref.getLong(challengeDateKey, -1L)
        return if (epochDay == -1L) null else LocalDate.ofEpochDay(epochDay)
    }

    fun saveChallengeToSharedPref(challenge: Challenge) {
        with(sharedPref.edit()) {
            putInt(challengeIdKey, challenge.challengeId)
            putLong(challengeDateKey, LocalDate.now().toEpochDay())
            apply()
        }
    }

    fun removeChallengeFromSharedPref() {
        with(sharedPref.edit()) {
            remove(challengeIdKey)
            remove(challengeDateKey)
            apply()
        }
    }
}