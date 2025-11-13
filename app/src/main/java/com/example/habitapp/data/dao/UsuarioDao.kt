package com.example.habitapp.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.habitapp.data.entity.Usuario
import com.example.habitapp.data.relation.UsuarioConHabitosYTareas

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE idUsuario = :id")
    suspend fun getById(id: Long): Usuario?

    @Query("SELECT * FROM usuario ORDER BY nombre ASC")
    fun getAll(): Flow<List<Usuario>>

    @Transaction
    @Query("SELECT * FROM usuario WHERE idUsuario = :id")
    fun getUsuarioConHabitosYTareas(id: Long): Flow<UsuarioConHabitosYTareas?>
}
