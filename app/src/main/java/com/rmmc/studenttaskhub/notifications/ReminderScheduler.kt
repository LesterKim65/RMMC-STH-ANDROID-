package com.rmmc.studenttaskhub.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rmmc.studenttaskhub.data.model.Schedule
import com.rmmc.studenttaskhub.data.model.Task

object ReminderScheduler {
    private const val TYPE_TASK = "TASK"
    private const val TYPE_CLASS = "CLASS"
    private const val WEEK_MILLIS = 7L * 24 * 60 * 60 * 1000

    fun scheduleTaskReminder(context: Context, task: Task) {
        val triggerMillis = task.reminderMillis ?: return
        if (triggerMillis <= System.currentTimeMillis()) return
        schedule(
            context = context,
            requestCode = 10_000 + task.id,
            triggerMillis = triggerMillis,
            title = context.getString(com.rmmc.studenttaskhub.R.string.task_reminder_title),
            message = context.getString(
                com.rmmc.studenttaskhub.R.string.task_reminder_message,
                task.title
            ),
            type = TYPE_TASK,
            entityId = task.id
        )
    }

    fun scheduleClassReminder(context: Context, schedule: Schedule) {
        val triggerMillis = schedule.classStartReminderMillis ?: return
        val firstTrigger = if (triggerMillis <= System.currentTimeMillis()) {
            triggerMillis + WEEK_MILLIS
        } else {
            triggerMillis
        }
        scheduleRepeating(
            context = context,
            requestCode = 20_000 + schedule.id,
            firstTriggerMillis = firstTrigger,
            intervalMillis = WEEK_MILLIS,
            title = context.getString(com.rmmc.studenttaskhub.R.string.class_reminder_title),
            message = context.getString(
                com.rmmc.studenttaskhub.R.string.class_reminder_message,
                schedule.subject,
                schedule.room
            ),
            type = TYPE_CLASS,
            entityId = schedule.id
        )
    }

    fun cancelTaskReminder(context: Context, taskId: Int) {
        cancel(context, 10_000 + taskId)
    }

    fun cancelClassReminder(context: Context, scheduleId: Int) {
        cancel(context, 20_000 + scheduleId)
    }

    private fun schedule(
        context: Context,
        requestCode: Int,
        triggerMillis: Long,
        title: String,
        message: String,
        type: String,
        entityId: Int
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, title)
            putExtra(ReminderReceiver.EXTRA_MESSAGE, message)
            putExtra(ReminderReceiver.EXTRA_TYPE, type)
            putExtra(ReminderReceiver.EXTRA_ENTITY_ID, entityId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        }
    }

    private fun scheduleRepeating(
        context: Context,
        requestCode: Int,
        firstTriggerMillis: Long,
        intervalMillis: Long,
        title: String,
        message: String,
        type: String,
        entityId: Int
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, title)
            putExtra(ReminderReceiver.EXTRA_MESSAGE, message)
            putExtra(ReminderReceiver.EXTRA_TYPE, type)
            putExtra(ReminderReceiver.EXTRA_ENTITY_ID, entityId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            firstTriggerMillis,
            intervalMillis,
            pendingIntent
        )
    }

    private fun cancel(context: Context, requestCode: Int) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
