package com.rmmc.studenttaskhub.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rmmc.studenttaskhub.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val db = AppDatabase.getInstance(context)
                    val tasks = db.taskDao().getTasksWithReminders()
                    tasks.forEach { task ->
                        ReminderScheduler.scheduleTaskReminder(context, task)
                    }

                    val schedules = db.scheduleDao().getSchedulesWithReminders()
                    schedules.forEach { schedule ->
                        ReminderScheduler.scheduleClassReminder(context, schedule)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
