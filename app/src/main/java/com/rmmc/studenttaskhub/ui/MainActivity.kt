package com.rmmc.studenttaskhub.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.rmmc.studenttaskhub.R
import com.rmmc.studenttaskhub.databinding.ActivityMainBinding
import com.rmmc.studenttaskhub.notifications.NotificationHelper
import com.rmmc.studenttaskhub.ui.dashboard.DashboardFragment
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
            showFragment(DashboardFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.dashboardFragment -> showFragment(DashboardFragment())
                R.id.tasksFragment -> showFragment(TaskListFragment())
                R.id.scheduleFragment -> showFragment(ScheduleFragment())
                else -> false
            }
        }

        binding.bottomNav.selectedItemId = R.id.dashboardFragment

        ensureNotificationPermission()
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
