package com.rmmc.studenttaskhub.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rmmc.studenttaskhub.ui.AlarmActivity

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val notificationsEnabled = sharedPref.getBoolean("notifications_enabled", true)

        NotificationHelper.createChannel(context)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: return
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: return
        val entityId = intent.getIntExtra(EXTRA_ENTITY_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE).orEmpty()
        val soundUri = intent.getStringExtra(EXTRA_SOUND_URI)
        
        // Alarms (Schedules) trigger regardless of the "Enable Notifications" switch
        if (type == "CLASS") {
            val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_MESSAGE, message)
                putExtra(EXTRA_SOUND_URI, soundUri)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            context.startActivity(alarmIntent)
        }

        // Standard Notifications (Assignments) only show if the switch is ON
        if (notificationsEnabled) {
            val id = "${type}_${entityId}".hashCode()
            NotificationHelper.showReminder(context, id, title, message, soundUri)
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_ENTITY_ID = "extra_entity_id"
        const val EXTRA_SOUND_URI = "extra_sound_uri"
    }
}
