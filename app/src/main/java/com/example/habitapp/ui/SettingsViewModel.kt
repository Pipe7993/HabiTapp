package com.example.habitapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    data class State(
        val notificationsEnabled: Boolean = true,
        val darkModeEnabled: Boolean = false,
        val remindersEnabled: Boolean = false,
    )

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state

    fun setNotifications(enabled: Boolean) {
        _state.value = _state.value?.copy(notificationsEnabled = enabled)
    }
    fun setDarkMode(enabled: Boolean) {
        _state.value = _state.value?.copy(darkModeEnabled = enabled)
    }
    fun setReminders(enabled: Boolean) {
        _state.value = _state.value?.copy(remindersEnabled = enabled)
    }
}

