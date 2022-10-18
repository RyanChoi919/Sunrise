package com.nodes.sunrise.components.helpers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.nodes.sunrise.MainActivity
import com.nodes.sunrise.R
import com.nodes.sunrise.components.broadcastReceivers.AlarmReceiver
import com.nodes.sunrise.enums.NotiChannel
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class NotificationHelper(val context: Context) {

    private val TAG = this::class.java.simpleName + ".TAG"

    private val manager by lazy {
        NotificationManagerCompat.from(context)
    }

    private val writeEntryPendingIntent = createPendingIntentToFragment(R.id.nav_entry_write)


    fun createNotificationChannels() {
        if (NotiChannel.values().isNotEmpty()) {
            val channels = NotiChannel.values()
                .map { enum ->
                    NotificationChannel(
                        enum.getId(context),
                        enum.getChannelName(context),
                        enum.importance
                    ).apply {
                        if (enum.description != null) {
                            description = enum.description
                        }
                    }
                }
            manager.createNotificationChannels(channels)
        }
    }

    fun notifyToWriteEntry() {
        val notificationToWriteEntryId = 1
        val notificationToWriteEntry =
            NotificationCompat.Builder(context, NotiChannel.WRITE_ENTRY.getId(context))
                .setSmallIcon(R.drawable.ic_round_edit_24)
                .setContentTitle("일기 쓰기 알림")
                .setContentText("일기를 작성할 시간입니다.")
                .setContentIntent(writeEntryPendingIntent)
                .setAutoCancel(true)
                .build()

        manager.notify(notificationToWriteEntryId, notificationToWriteEntry)
    }

    fun setNotificationRepeating() {
        val sharedPreferenceHelper = SharedPreferenceHelper(context)

        with(sharedPreferenceHelper) {
            val isNotificationEnabled = getSavedNotificationEnabled()
            Log.d(TAG, "setNotificationRepeating: isNotificationEnabled = $isNotificationEnabled")

            if (isNotificationEnabled) {
                val selectedSecondsOfDay = getSavedNotificationStartSecondOfDay()
                if (selectedSecondsOfDay != null) {
                    val selectedHour = selectedSecondsOfDay / 3600
                    val selectedMinute = selectedSecondsOfDay % 3600 / 60
                    Log.d(
                        TAG,
                        "setNotificationRepeating: selectedHourAndMinute = $selectedHour : $selectedMinute"
                    )

                    // 요일 및 시간 값 변형
                    var selectedDateTime =
                        LocalDateTime.now().withHour(selectedHour).withMinute(selectedMinute)
                            .withSecond(0).withNano(0)

                    // 알림 설정 시간이 현재 시간보다 이전인 경우, 다음날 알림이 울리도록 설정
                    val isBeforeCurrent = selectedDateTime.isBefore(LocalDateTime.now())
                    Log.d(TAG, "setNotificationRepeating: isBeforeCurrent? = $isBeforeCurrent")
                    if (isBeforeCurrent) {
                        selectedDateTime = selectedDateTime.plusDays(1)
                        Log.d(TAG, "setNotificationRepeating: selectedDateTime = $selectedDateTime")
                    }

                    val millis =
                        ZonedDateTime.of(selectedDateTime, ZoneId.systemDefault()).toInstant()
                            .toEpochMilli()
                    Log.d(TAG, "setNotificationRepeating: millis = $millis")

                    // 알람 매니저 인스턴스 생성
                    val alarmManager =
                        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                    // 알람 리시버를 위한 PendingIntent 인스턴스 생성
                    val pendingIntent = createPendingIntentForAlarmReceiver(getDowBooleanArray())

                    // 알람 매니저에 Repeating 정보 등록
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        millis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                }
            }
        }
    }

    fun updateNotificationRepeating() {
        Log.d(TAG, "updateNotificationRepeating: called")
        cancelNotificationRepeating()
        setNotificationRepeating()
    }

    fun cancelNotificationRepeating() {
        val pendingIntent = createPendingIntentForAlarmReceiver(getDowBooleanArray())
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "cancelNotificationRepeating: $pendingIntent has been canceled")
    }

    private fun createPendingIntentToFragment(
        destinationId: Int,
        args: Bundle? = null
    ): PendingIntent {
        Log.d(TAG, "createPendingIntentToFragment: called")
        // using Navigation Component, can open a specific destination using NavDeepLinkBuilder
        return NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(destinationId)
            .setArguments(args)
            .createPendingIntent()
    }

    private fun getDowBooleanArray(): BooleanArray {
        val selectedDowValue = SharedPreferenceHelper(context).getSavedNotificationDowValues()
        Log.d(TAG, "getDowBooleanArray: selectedDowValue = $selectedDowValue")
        return convertDowValueSetToBooleanArray(selectedDowValue!!)
    }

    private fun convertDowValueSetToBooleanArray(selectedDowValue: Set<String>): BooleanArray {
        val map = convertDowValueSetToMap(selectedDowValue)
        return map.values.toBooleanArray()
    }

    private fun getDowMap(): Map<DayOfWeek, Boolean> {
        val selectedDowValue = SharedPreferenceHelper(context).getSavedNotificationDowValues()
        return convertDowValueSetToMap(selectedDowValue!!)
    }

    private fun convertDowValueSetToMap(selectedDowValue: Set<String>): Map<DayOfWeek, Boolean> {
        val dowArray =
            context.resources.getStringArray(R.array.pref_day_of_the_week_values)
        return dowArray.associateBy(
            {
                when (it) {
                    context.getString(R.string.pref_notification_dow_mon_value) -> DayOfWeek.MONDAY
                    context.getString(R.string.pref_notification_dow_tue_value) -> DayOfWeek.TUESDAY
                    context.getString(R.string.pref_notification_dow_wed_value) -> DayOfWeek.WEDNESDAY
                    context.getString(R.string.pref_notification_dow_thu_value) -> DayOfWeek.THURSDAY
                    context.getString(R.string.pref_notification_dow_fri_value) -> DayOfWeek.FRIDAY
                    context.getString(R.string.pref_notification_dow_sat_value) -> DayOfWeek.SATURDAY
                    else -> DayOfWeek.SUNDAY
                }
            }, { selectedDowValue.contains(it) }
        )
    }

    private fun createPendingIntentForAlarmReceiver(dowBooleanArray: BooleanArray): PendingIntent {
        val requestCode = 1

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_NOTIFICATION_DOW, dowBooleanArray)
        }
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, flags)
        Log.d(TAG, "createPendingIntentForAlarmReceiver: pendingIntent = $pendingIntent")
        return pendingIntent
    }
}