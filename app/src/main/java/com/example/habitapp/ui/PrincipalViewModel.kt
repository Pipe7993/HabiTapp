package com.example.habitapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import com.example.habitapp.model.Habito
import com.example.habitapp.model.Tarea
import java.util.Date

class PrincipalViewModel : ViewModel() {
    data class UiState(
        val habitos: List<Habito> = emptyList(),
        val tareas: List<Tarea> = emptyList()
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

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
        _state.update { it.copy(habitos = it.habitos + nuevo) }
    }

    fun alternarHabito(id: Int) {
        _state.update { current ->
            val actualizados = current.habitos.map { h ->
                if (h.id == id) h.copy(Estado = if (h.Estado == "Pendiente") "Completo" else "Pendiente") else h
            }
            current.copy(habitos = actualizados)
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
        _state.update { it.copy(tareas = it.tareas + nueva) }
    }

    fun alternarTarea(id: Int) {
        _state.update { current ->
            val actualizadas = current.tareas.map { t ->
                if (t.id == id) t.copy(completado = !t.completado) else t
            }
            current.copy(tareas = actualizadas)
        }
    }
}
