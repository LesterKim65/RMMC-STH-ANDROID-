package com.rmmc.studenttaskhub.ui

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rmmc.studenttaskhub.R
import com.rmmc.studenttaskhub.databinding.ActivityMainBinding
import com.rmmc.studenttaskhub.notifications.NotificationHelper
import com.rmmc.studenttaskhub.ui.schedule.ScheduleFragment
import com.rmmc.studenttaskhub.ui.tasks.TaskListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NotificationHelper.createChannel(this)

        if (savedInstanceState == null) {
            showFragment(ScheduleFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.scheduleFragment -> showFragment(ScheduleFragment())
                R.id.tasksFragment -> showFragment(TaskListFragment())
                else -> false
            }
        }

        binding.bottomNav.selectedItemId = R.id.scheduleFragment

        ensureNotificationPermission()
        ensureExactAlarmPermission()
    }

    private fun ensureExactAlarmPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.fromParts("package", packageName, null)
            }
            startActivity(intent)
            Toast.makeText(this, getString(R.string.exact_alarm_permission_needed), Toast.LENGTH_LONG).show()
        }
    }

    fun navigateToTasks() {
        binding.bottomNav.selectedItemId = R.id.tasksFragment
    }

    fun navigateToSchedule() {
        binding.bottomNav.selectedItemId = R.id.scheduleFragment
    }

    private fun showFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return true
    }

    private fun ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            REQUEST_NOTIFICATIONS
        )
    }

    companion object {
        private const val REQUEST_NOTIFICATIONS = 3001
    }
}
