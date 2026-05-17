package com.rmmc.studenttaskhub.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rmmc.studenttaskhub.R

object NotificationHelper {
    const val CHANNEL_ID = "rmmc_task_hub_reminders"

    fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_description)
            // Note: Channel sound is only a fallback for Android 8.0+. 
            // We set it on the notification itself for more flexibility.
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun showReminder(context: Context, id: Int, title: String, message: String, soundUriString: String? = null) {
        val soundUri = soundUriString?.let { Uri.parse(it) }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        soundUri?.let {
            notificationBuilder.setSound(it)
        }

        NotificationManagerCompat.from(context).notify(id, notificationBuilder.build())
    }
}
