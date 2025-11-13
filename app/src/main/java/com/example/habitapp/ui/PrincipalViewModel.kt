package com.example.habitapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitapp.model.Habito
import com.example.habitapp.model.Tarea
import java.util.Date

class PrincipalViewModel : ViewModel() {
    private val _listaHabitos = MutableLiveData<List<Habito>>(emptyList())
    val listaHabitos: LiveData<List<Habito>> = _listaHabitos

    private val _listaTareas = MutableLiveData<List<Tarea>>(emptyList())
    val listaTareas: LiveData<List<Tarea>> = _listaTareas

    private var siguienteIdHabito = 1
    private var siguienteIdTarea = 1

    fun agregarHabito(titulo: String) {
        val nuevo = Habito(
            id = siguienteIdHabito++,
            Nombre = titulo,
            Descripcion = "",
            Frecuencia = "",
            Progreso = 0,
            Estado = "Pendiente"
        )
        _listaHabitos.value = _listaHabitos.value.orEmpty() + nuevo
    }

    fun alternarHabito(id: Int) {
        _listaHabitos.value = _listaHabitos.value.orEmpty().map {
            if (it.id == id) it.copy(Estado = if (it.Estado == "Pendiente") "Completo" else "Pendiente") else it
        }
    }

    fun agregarTarea(titulo: String) {
        val nueva = Tarea(
            id = siguienteIdTarea++,
            Titulo = titulo,
            Descripcion = "",
            FechaLimite = Date(),
            Prioridad = 0,
            Estado = "Pendiente"
        )
        _listaTareas.value = _listaTareas.value.orEmpty() + nueva
    }

    fun alternarTarea(id: Int) {
        _listaTareas.value = _listaTareas.value.orEmpty().map {
            if (it.id == id) it.copy(completado = !it.completado) else it
        }
    }
}
