package com.nodes.sunrise.enums

import android.content.Context
import androidx.annotation.StringRes
import com.nodes.sunrise.R
import java.io.IOException

enum class PrefKeys(@StringRes private val keyResId: Int) {
    THEME(R.string.pref_theme_key),
    FONT(R.string.pref_key_font),
    NOTIFICATION_ENABLED(R.string.pref_notification_enabled_key),
    NOTIFICATION_DOW(R.string.pref_notification_dow_key),
    NOTIFICATION_TIME(R.string.pref_notification_time_key),
    VERSION_INFO(R.string.pref_version_info_key),
    OSS_LICENSES(R.string.pref_oss_licenses_key),
    PREMIUM(R.string.pref_key_premium);

    fun getKeyString(context: Context): String {
        return context.getString(keyResId)
    }

    companion object {
        fun getPrefKeysFromKeyString(context: Context, keyString: String): PrefKeys? {
            return try {
                values().asList().stream().filter { key -> key.getKeyString(context) == keyString }
                    .findFirst().get()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}