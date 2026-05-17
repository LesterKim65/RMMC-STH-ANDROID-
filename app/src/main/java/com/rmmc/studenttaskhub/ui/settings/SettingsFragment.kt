package com.rmmc.studenttaskhub.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.rmmc.studenttaskhub.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        
        // Theme Switch
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)
        binding.darkModeSwitch.isChecked = isDarkMode
        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean("dark_mode", isChecked)
            editor.apply()
            
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Notification Switch
        val notificationsEnabled = sharedPref.getBoolean("notifications_enabled", true)
        binding.notificationSwitch.isChecked = notificationsEnabled
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean("notifications_enabled", isChecked).apply()
            // In a real app, you might want to cancel/reschedule all alarms here
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
