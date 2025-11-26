package com.example.habitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Task(val id: Long, val title: String, val subtitle: String, val time: String, val priority: String, val done: Boolean = false)

class TasksViewModel : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    init {
        _tasks.value = listOf(
            Task(1, "Reunión de equipo", "Revisar avances del proyecto", "10:00", "Media", true),
            Task(2, "Terminar propuesta proyecto", "Completar documento final", "14:00", "Alta", false),
            Task(3, "Compras del supermercado", "Lista de compras semanal", "17:00", "Baja", false),
            Task(4, "Ir al gimnasio", "Rutina de piernas", "19:00", "Media", false)
        )
    }

    fun toggleDone(id: Long) {
        _tasks.value = _tasks.value.orEmpty().map { if (it.id == id) it.copy(done = !it.done) else it }
    }

    fun addTask(task: Task) {
        _tasks.value = _tasks.value.orEmpty() + task
    }

    fun removeTask(id: Long) {
        _tasks.value = _tasks.value.orEmpty().filterNot { it.id == id }
    }
}

