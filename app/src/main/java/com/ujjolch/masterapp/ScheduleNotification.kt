package com.ujjolch.masterapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

fun scheduleNotification(context: Context, hours:Int,minutes:Int, message: String):String {

// Create a Calendar instance for the scheduled time
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hours)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
// If this time is in the past, add one day to schedule for the next day
    if (calendar.timeInMillis <= System.currentTimeMillis()) {
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }

// Get the time in milliseconds for when the alarm should trigger
    val triggerTimeMillis = calendar.timeInMillis
    Log.d("SNotification", "Alarm set for: ${calendar.time}")

// Create an intent that points to our BroadcastReceiver class
    val intent = Intent(context, ReminderBroadcast::class.java).apply {
        putExtra("EXTRA_MESSAGE", message)
    }

    // Use a unique request code for each notification.
    val requestCode = System.currentTimeMillis().toInt()

// Create a PendingIntent that will perform a broadcast
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

// Get the AlarmManager service
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

// Schedule the alarm for the specified time. Using setExactAndAllowWhileIdle ensures exact timing.
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        triggerTimeMillis,
        pendingIntent
    )

    return "Success scheduling the message: $message"

}



