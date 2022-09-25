package com.nodes.sunrise.enums

import android.app.NotificationManager
import android.content.Context
import com.nodes.sunrise.R

enum class NotiChannel(
    val idResId: Int,
    val channelNameResId: Int,
    val description: String? = null,
    val importance: Int
) {
    WRITE_ENTRY(
        idResId = R.string.notification_channel_write_entry_id,
        channelNameResId = R.string.notification_channel_write_entry_channel_name,
        importance = NotificationManager.IMPORTANCE_HIGH
    );

    fun getId(context: Context): String {
        return context.resources.getString(idResId)
    }

    fun getChannelName(context: Context): CharSequence {
        return context.resources.getString(channelNameResId)
    }
}