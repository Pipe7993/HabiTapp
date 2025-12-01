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

    private var nextId: Int = 1

    fun addHabit(name: String, desc: String) {
        if (name.isBlank()) {
            _addState.value = AddState(success = false, error = "El nombre es obligatorio")
            return
        }
        val nueva = Habito(
            id = nextId++,
            Nombre = name,
            Descripcion = desc,
            Frecuencia = "Diario",
            Progreso = 0,
            Estado = "Activo"
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

    fun updateHabit(name: String, desc: String) {
        val current = _selectedHabit.value ?: return
        val updated = current.copy(Nombre = name, Descripcion = desc)
        val lista = _habits.value?.map { if (it.id == current.id) updated else it } ?: emptyList()
        _habits.value = lista
        _selectedHabit.value = updated
    }
}
