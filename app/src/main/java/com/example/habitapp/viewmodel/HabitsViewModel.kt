package com.example.habitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Habit(val id: Long, val title: String, val description: String, val progress: Int)

class HabitsViewModel : ViewModel() {
    private val _habits = MutableLiveData<List<Habit>>(emptyList())
    val habits: LiveData<List<Habit>> = _habits

    init {
        _habits.value = listOf(
            Habit(1, "Meditación matutina", "Meditar 10 minutos cada mañana", 85),
            Habit(2, "Beber agua", "8 vasos de agua al día", 62),
            Habit(3, "Ejercicio", "30 minutos de actividad física", 40),
            Habit(4, "Leer", "Leer 20 páginas antes de dormir", 70)
        )
    }

    fun addHabit(habit: Habit) {
        _habits.value = _habits.value.orEmpty() + habit
    }

    fun removeHabit(id: Long) {
        _habits.value = _habits.value.orEmpty().filterNot { it.id == id }
    }

    fun updateProgress(id: Long, progress: Int) {
        _habits.value = _habits.value.orEmpty().map {
            if (it.id == id) it.copy(progress = progress) else it
        }
    }
}

