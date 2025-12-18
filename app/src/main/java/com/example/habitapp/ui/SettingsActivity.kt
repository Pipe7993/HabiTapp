package com.example.habitapp.ui

import android.os.Bundle
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.example.habitapp.R

class SettingsActivity : AppCompatActivity() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toggleNotifications = findViewById<Switch>(R.id.toggle_notifications)
        val toggleDarkMode = findViewById<Switch>(R.id.toggle_dark_mode)
        val toggleReminders = findViewById<Switch>(R.id.toggle_reminders)

        viewModel.initPrefs(this)

        viewModel.state.observe(this, Observer { state ->
            toggleNotifications.isChecked = state.notificationsEnabled
            toggleDarkMode.isChecked = state.darkModeEnabled
            toggleReminders.isChecked = state.remindersEnabled
        })

        toggleNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotifications(isChecked)
        }
        toggleDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        toggleReminders.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setReminders(isChecked)
        }
    }
}
