package com.example.habitapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitapp.data.db.AppDatabase
import com.example.habitapp.data.entity.Tarea
import com.example.habitapp.data.entity.EstadoTarea
import com.example.habitapp.data.repository.TareaRepository
import java.util.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TareaRoomViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repo = TareaRepository(db.tareaDao())

    val tareas = repo.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insert(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) { repo.insert(tarea) }
    }

    suspend fun insertAndReturnId(tarea: Tarea): Long {
        return repo.insert(tarea)
    }

    fun update(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) { repo.update(tarea) }
    }

    fun delete(tarea: Tarea) {
        viewModelScope.launch(Dispatchers.IO) { repo.delete(tarea) }
    }

    fun cambiarEstado(id: Long, estado: com.example.habitapp.data.entity.EstadoTarea) {
        viewModelScope.launch(Dispatchers.IO) { repo.cambiarEstado(id, estado) }
    }

    suspend fun getById(id: Long): Tarea? = repo.getById(id)

    fun getCompletadasUltimoMes(): kotlinx.coroutines.flow.Flow<List<Tarea>> {
        val cal = Calendar.getInstance()
        val fin = cal.timeInMillis
        cal.add(Calendar.MONTH, -1)
        val inicio = cal.timeInMillis
        return db.tareaDao().getCompletadasEnRango(EstadoTarea.COMPLETADA, inicio, fin)
    }
    fun getPendientes(): kotlinx.coroutines.flow.Flow<List<Tarea>> = db.tareaDao().getPorEstado(EstadoTarea.PENDIENTE)
    fun getParaHoy(): kotlinx.coroutines.flow.Flow<List<Tarea>> = db.tareaDao().getParaHoy(EstadoTarea.PENDIENTE)
}
