package com.nodes.sunrise.enums

import android.content.Context
import com.nodes.sunrise.R

enum class PrefKeys(private val keyResId: Int) {
    THEME(R.string.pref_theme_key),
    NOTIFICATION_ENABLED(R.string.pref_notification_enabled_key),
    NOTIFICATION_DOW(R.string.pref_notification_dow_key),
    NOTIFICATION_TIME(R.string.pref_notification_time_key),
    VERSION_INFO(R.string.pref_version_info_key),
    OSS_LICENSES(R.string.pref_oss_licenses_key);

    fun getKeyString(context: Context): String {
        return context.getString(keyResId)
    }
}