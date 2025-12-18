package com.example.habitapp.ui

import android.content.Context
import android.content.SharedPreferences
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

    private var prefs: SharedPreferences? = null
    fun initPrefs(context: Context) {
        prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        _state.value = State(
            notificationsEnabled = prefs?.getBoolean("notificationsEnabled", true) ?: true,
            darkModeEnabled = prefs?.getBoolean("darkModeEnabled", false) ?: false,
            remindersEnabled = prefs?.getBoolean("remindersEnabled", false) ?: false
        )
    }
    override fun onCleared() {
        super.onCleared()
        prefs = null
    }
    fun setNotifications(enabled: Boolean) {
        _state.value = _state.value?.copy(notificationsEnabled = enabled)
        prefs?.edit()?.putBoolean("notificationsEnabled", enabled)?.apply()
    }
    fun setDarkMode(enabled: Boolean) {
        _state.value = _state.value?.copy(darkModeEnabled = enabled)
        prefs?.edit()?.putBoolean("darkModeEnabled", enabled)?.apply()
    }
    fun setReminders(enabled: Boolean) {
        _state.value = _state.value?.copy(remindersEnabled = enabled)
        prefs?.edit()?.putBoolean("remindersEnabled", enabled)?.apply()
    }
}
