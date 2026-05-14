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
import androidx.appcompat.app.AppCompatDelegate
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
        // Load theme preference before super.onCreate to avoid flicker
        loadThemePreference()
        
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

        updateThemeIcon()
        binding.themeToggleButton.setOnClickListener {
            toggleTheme()
        }

        ensureNotificationPermission()
        ensureExactAlarmPermission()
    }

    private fun updateThemeIcon() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val isDarkMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED || currentMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
        } else {
            currentMode == AppCompatDelegate.MODE_NIGHT_YES
        }
        
        binding.themeToggleButton.setImageResource(
            if (isDarkMode) R.drawable.ic_light_mode else R.drawable.ic_dark_mode
        )
    }

    private fun loadThemePreference() {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun toggleTheme() {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        val editor = sharedPref.edit()
        
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putBoolean("dark_mode", false)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putBoolean("dark_mode", true)
        }
        editor.apply()
        // Activity will recreate automatically
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
