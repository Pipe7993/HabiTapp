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
            Task(1, "Reunión de equipo", "Revisar avances del proyecto", "30/11/2025", "Media", true),
            Task(2, "Terminar propuesta proyecto", "Completar documento final", "01/12/2025", "Alta", false),
            Task(3, "Compras del supermercado", "Lista de compras semanal", "02/12/2025", "Baja", false),
            Task(4, "Ir al gimnasio", "Rutina de piernas", "30/11/2025", "Media", false)
        )
    }

    fun toggleDone(id: Long) {
        _tasks.value = _tasks.value.orEmpty().map { if (it.id == id) it.copy(done = !it.done) else it }
    }

    fun addTask(task: Task) {
        _tasks.value = _tasks.value.orEmpty() + task
    }

    fun updateTask(id: Long, title: String, subtitle: String, time: String, priority: String) {
        _tasks.value = _tasks.value.orEmpty().map {
            if (it.id == id) {
                it.copy(
                    title = title,
                    subtitle = subtitle,
                    time = time,
                    priority = priority
                )
            } else {
                it
            }
        }
    }

    fun removeTask(id: Long) {
        _tasks.value = _tasks.value.orEmpty().filterNot { it.id == id }
    }
}

