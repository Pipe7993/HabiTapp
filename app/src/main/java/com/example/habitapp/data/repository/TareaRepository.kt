package com.example.habitapp.data.repository

import com.example.habitapp.data.dao.TareaDao
import com.example.habitapp.data.entity.Tarea
import kotlinx.coroutines.flow.Flow

class TareaRepository(private val tareaDao: TareaDao) {
    fun getAll(): Flow<List<Tarea>> = tareaDao.getAll()

    fun getByUsuario(idUsuario: Long): Flow<List<Tarea>> = tareaDao.getTareasByUsuario(idUsuario)

    suspend fun getById(id: Long): Tarea? = tareaDao.getById(id)

    suspend fun insert(tarea: Tarea): Long = tareaDao.insert(tarea)

    suspend fun update(tarea: Tarea) = tareaDao.update(tarea)

    suspend fun delete(tarea: Tarea) = tareaDao.delete(tarea)

    suspend fun cambiarEstado(id: Long, estado: com.example.habitapp.data.entity.EstadoTarea) = tareaDao.cambiarEstado(id, estado)
}

