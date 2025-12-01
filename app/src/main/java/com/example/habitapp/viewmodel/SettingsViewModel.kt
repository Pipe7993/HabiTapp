package com.example.habitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class SettingsState(val notifications: Boolean = true, val darkMode: Boolean = false, val reminders: Boolean = true)

class SettingsViewModel : ViewModel() {
    private val _state = MutableLiveData(SettingsState())
    val state: LiveData<SettingsState> = _state

    fun setNotifications(enabled: Boolean) { _state.value = _state.value?.copy(notifications = enabled) }
    fun setDarkMode(enabled: Boolean) { _state.value = _state.value?.copy(darkMode = enabled) }
    fun setReminders(enabled: Boolean) { _state.value = _state.value?.copy(reminders = enabled) }
}

