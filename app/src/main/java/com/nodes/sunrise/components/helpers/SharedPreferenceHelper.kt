package com.nodes.sunrise.components.helpers

import android.content.Context
import androidx.preference.PreferenceManager
import com.nodes.sunrise.R
import com.nodes.sunrise.db.entity.Challenge
import java.time.LocalDate

class SharedPreferenceHelper(val context: Context) {

    private val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
    private val challengeIdKey: String = getString(R.string.pref_key_challenge_id)
    private val challengeDateKey: String = getString(R.string.pref_key_challenge_date)
    private val keyNotificationEnabled: String = getString(R.string.pref_notification_enabled_key)
    private val keyNotificationDow: String = getString(R.string.pref_notification_dow_key)
    private val keyNotificationTime: String = getString(R.string.pref_notification_time_key)
    private val keyShouldEnableTitleByDefault: String =
        getString(R.string.pref_key_enable_title_by_default)
    private val keyShouldAddLocationByDefault: String =
        getString(R.string.pref_key_add_location_by_default)
    private val keyFont: String = getString(R.string.pref_key_font)

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

    fun saveFont(fontResId: Int) {
        with(sharedPref.edit()) {
            putInt(keyFont, fontResId)
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

    fun getSavedNotificationEnabled(): Boolean {
        return sharedPref.getBoolean(keyNotificationEnabled, false)
    }

    fun getSavedNotificationDowValues(): Set<String>? {
        val valueSet = sharedPref.getStringSet(keyNotificationDow, emptySet<String>())
        return if (valueSet != null && valueSet.isNotEmpty()) valueSet else null
    }

    fun getSavedNotificationStartSecondOfDay(): Int? {
        val value = sharedPref.getInt(keyNotificationTime, -1)
        return if (value >= 0) value else null
    }

    fun getSavedShouldEnableTitleByDefault(): Boolean {
        return sharedPref.getBoolean(keyShouldEnableTitleByDefault, true)
    }

    fun getSavedShouldAddLocationByDefault(): Boolean {
        return sharedPref.getBoolean(keyShouldAddLocationByDefault, true)
    }

    fun getSavedFont(): Int {
        return sharedPref.getInt(keyFont, R.font.nanum_myeongjo)
    }

    private fun getString(resId: Int): String {
        return context.resources.getString(resId)
    }
}