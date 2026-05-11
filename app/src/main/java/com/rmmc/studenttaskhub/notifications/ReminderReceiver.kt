package com.rmmc.studenttaskhub.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationHelper.createChannel(context)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: return
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: return
        val entityId = intent.getIntExtra(EXTRA_ENTITY_ID, 0)
        val type = intent.getStringExtra(EXTRA_TYPE).orEmpty()
        val id = "${type}_${entityId}".hashCode()
        NotificationHelper.showReminder(context, id, title, message)
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_ENTITY_ID = "extra_entity_id"
    }
}
