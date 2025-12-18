package com.example.habitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class SettingsState(val notifications: Boolean = true, val darkMode: Boolean = false, val reminders: Boolean = true)

object DaoProvider {
    lateinit var usuarioDao: com.example.habitapp.data.dao.UsuarioDao
}

class SettingsViewModel : ViewModel() {
    private val _state = MutableLiveData(SettingsState())
    val state: LiveData<SettingsState> = _state

    private val _nombreUsuario = MutableLiveData<String>()
    val nombreUsuario: LiveData<String> = _nombreUsuario

    fun cargarNombreUsuario(id: Long) {
        viewModelScope.launch {
            val usuario = DaoProvider.usuarioDao.getById(id)
            _nombreUsuario.postValue(usuario?.nombre ?: "")
        }
    }

    fun actualizarNombreUsuario(id: Long, nuevoNombre: String) {
        viewModelScope.launch {
            val usuario = DaoProvider.usuarioDao.getById(id)
            if (usuario != null) {
                DaoProvider.usuarioDao.update(usuario.copy(nombre = nuevoNombre))
                _nombreUsuario.postValue(nuevoNombre)
            }
        }
    }

    fun setNotifications(enabled: Boolean) { _state.value = _state.value?.copy(notifications = enabled) }
    fun setDarkMode(enabled: Boolean) { _state.value = _state.value?.copy(darkMode = enabled) }
    fun setReminders(enabled: Boolean) { _state.value = _state.value?.copy(reminders = enabled) }
}
