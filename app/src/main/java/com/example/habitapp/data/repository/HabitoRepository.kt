package com.example.habitapp.data.repository

import com.example.habitapp.data.dao.HabitoDao
import com.example.habitapp.data.entity.Habito
import kotlinx.coroutines.flow.Flow

class HabitoRepository(private val habitoDao: HabitoDao) {
    fun getAll(): Flow<List<Habito>> = habitoDao.getAll()

    fun getByUsuario(idUsuario: Long): Flow<List<Habito>> = habitoDao.getHabitosByUsuario(idUsuario)

    suspend fun getById(id: Long): Habito? = habitoDao.getById(id)

    suspend fun insert(habito: Habito): Long = habitoDao.insert(habito)

    suspend fun update(habito: Habito) = habitoDao.update(habito)

    suspend fun delete(habito: Habito) = habitoDao.delete(habito)

    suspend fun marcarCompletado(id: Long, completado: Boolean) = habitoDao.marcarCompletado(id, completado)
}

