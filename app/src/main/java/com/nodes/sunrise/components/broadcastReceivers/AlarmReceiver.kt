package com.nodes.sunrise.components.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nodes.sunrise.components.helpers.NotificationHelper
import java.time.LocalDate

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_NOTIFICATION_DOW: String = "AlarmReceiver.EXTRA_NOTIFICATION_DOW"
        private const val TAG = "AlarmReceiver.TAG"
    }

    override fun onReceive(context: Context?, p1: Intent?) {
        Log.d(TAG, "onReceive: called")
        val dowBooleanArray =
            p1!!.getBooleanArrayExtra(EXTRA_NOTIFICATION_DOW) // mon = 0, ~ sun = 6 (same index as DayOfWeek)

        if (dowBooleanArray != null) {
            val shouldNotify = dowBooleanArray[LocalDate.now().dayOfWeek.ordinal]
            Log.d(TAG, "onReceive: shouldNotify = $shouldNotify")
            if (shouldNotify) {
                NotificationHelper(context!!).notifyToWriteEntry()
            }
        }
    }
}