package com.example.habitapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habitapp.model.Habito

class HabitsViewModel : ViewModel() {
    private val _habits = MutableLiveData<List<Habito>>(emptyList())
    val habits: LiveData<List<Habito>> = _habits

    private val _selectedHabit = MutableLiveData<Habito?>(null)
    val selectedHabit: LiveData<Habito?> = _selectedHabit

    data class AddState(val success: Boolean = false, val error: String? = null)
    private val _addState = MutableLiveData(AddState())
    val addState: LiveData<AddState> = _addState

    private var nextId: Int = 5

    init {
        _habits.value = listOf(
            Habito(1, "Meditación matutina", "Meditar 10 minutos cada mañana", "Diario", 85, "Activo", true),
            Habito(2, "Beber agua", "8 vasos de agua al día", "Diario", 62, "Activo", true),
            Habito(3, "Ejercicio", "30 minutos de actividad física", "Lunes, Miercoles, Viernes", 40, "Activo", true),
            Habito(4, "Leer", "Leer 20 páginas antes de dormir", "Diario", 70, "Activo", true)
        )
    }

    fun addHabit(name: String, desc: String, frequency: String) {
        if (name.isBlank()) {
            _addState.value = AddState(success = false, error = "El nombre es obligatorio")
            return
        }
        val nueva = Habito(
            id = nextId++,
            Nombre = name,
            Descripcion = desc,
            Frecuencia = frequency,
            Progreso = 0,
            Estado = "Activo",
            Activo = true
        )
        val lista = _habits.value?.toMutableList() ?: mutableListOf()
        lista.add(nueva)
        _habits.value = lista
        _addState.value = AddState(success = true)
    }

    fun selectHabit(id: Int) {
        val habit = _habits.value?.firstOrNull { it.id == id }
        _selectedHabit.value = habit
    }

    fun updateHabit(id: Int, name: String, desc: String, frequency: String) {
        val lista = _habits.value?.map {
            if (it.id == id) {
                it.copy(Nombre = name, Descripcion = desc, Frecuencia = frequency)
            } else {
                it
            }
        } ?: emptyList()
        _habits.value = lista
    }

    fun deleteHabit(id: Int) {
        _habits.value = _habits.value?.filter { it.id != id }
    }
}
