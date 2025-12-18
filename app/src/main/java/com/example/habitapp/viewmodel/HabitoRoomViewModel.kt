package com.example.habitapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitapp.data.db.AppDatabase
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.data.repository.HabitoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitoRoomViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    private val repo = HabitoRepository(db.habitoDao())

    val habitos = repo.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun insert(habito: Habito) {
        viewModelScope.launch(Dispatchers.IO) { repo.insert(habito) }
    }

    suspend fun insertAndReturnId(habito: Habito): Long {
        return repo.insert(habito)
    }

    fun update(habito: Habito) {
        viewModelScope.launch(Dispatchers.IO) { repo.update(habito) }
    }

    fun delete(habito: Habito) {
        viewModelScope.launch(Dispatchers.IO) { repo.delete(habito) }
    }

    fun marcarCompletado(id: Long, completado: Boolean) {
        viewModelScope.launch(Dispatchers.IO) { repo.marcarCompletado(id, completado) }
    }

    fun getCompletadosUltimoMes(): kotlinx.coroutines.flow.Flow<List<Habito>> {
        val cal = java.util.Calendar.getInstance()
        val fin = cal.timeInMillis
        cal.add(java.util.Calendar.MONTH, -1)
        val inicio = cal.timeInMillis
        return db.habitoDao().getCompletadosEnRango(inicio, fin)
    }
    fun getActivos(): kotlinx.coroutines.flow.Flow<List<Habito>> = db.habitoDao().getActivos()
}
