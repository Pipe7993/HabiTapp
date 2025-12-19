package com.example.habitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitapp.data.entity.Usuario
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

    private val _fotoUsuario = MutableLiveData<String?>()
    val fotoUsuario: LiveData<String?> = _fotoUsuario
    fun cargarUsuario(id: Long) {
        viewModelScope.launch {
            val usuario = ensureUsuario(id)
            _nombreUsuario.postValue(usuario.nombre)
            _fotoUsuario.postValue(usuario.fotoUri)
        }
    }

    fun actualizarNombreUsuario(id: Long, nuevoNombre: String) {
        viewModelScope.launch {
            val usuario = ensureUsuario(id)
            val actualizado = usuario.copy(nombre = nuevoNombre)
            DaoProvider.usuarioDao.update(actualizado)
            _nombreUsuario.postValue(nuevoNombre)
            _fotoUsuario.postValue(actualizado.fotoUri)
        }
    }

    fun actualizarFotoUsuario(id: Long, nuevaFotoUri: String?) {
        viewModelScope.launch {
            val usuario = ensureUsuario(id)
            val actualizado = usuario.copy(fotoUri = nuevaFotoUri)
            DaoProvider.usuarioDao.update(actualizado)
            _fotoUsuario.postValue(nuevaFotoUri)
            _nombreUsuario.postValue(actualizado.nombre)
        }
    }

    private suspend fun ensureUsuario(id: Long): Usuario {
        val existente = DaoProvider.usuarioDao.getById(id)
        if (existente != null) return existente

        val newId = DaoProvider.usuarioDao.insert(
            Usuario(nombre = "Usuario", email = "usuario@demo.com")
        )
        return DaoProvider.usuarioDao.getById(newId)
            ?: Usuario(idUsuario = newId, nombre = "Usuario", email = "usuario@demo.com")
    }

    fun setNotifications(enabled: Boolean) { _state.value = _state.value?.copy(notifications = enabled) }
    fun setDarkMode(enabled: Boolean) { _state.value = _state.value?.copy(darkMode = enabled) }
    fun setReminders(enabled: Boolean) { _state.value = _state.value?.copy(reminders = enabled) }
}
