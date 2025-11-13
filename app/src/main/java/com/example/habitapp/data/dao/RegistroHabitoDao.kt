package com.example.habitapp.data.dao

import androidx.room.*
import com.example.habitapp.data.entity.RegistroHabito
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroHabitoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(registro: RegistroHabito): Long

    @Update
    suspend fun update(registro: RegistroHabito)

    @Delete
    suspend fun delete(registro: RegistroHabito)

    @Query("SELECT * FROM registro_habito WHERE idRegistro = :id")
    suspend fun getById(id: Long): RegistroHabito?

    @Query("SELECT * FROM registro_habito ORDER BY fecha DESC")
    fun getAll(): Flow<List<RegistroHabito>>

    @Query("SELECT * FROM registro_habito WHERE idHabito = :idHabito ORDER BY fecha DESC")
    fun getRegistrosPorHabito(idHabito: Long): Flow<List<RegistroHabito>>

    @Query("SELECT COUNT(*) FROM registro_habito WHERE idHabito = :idHabito")
    suspend fun countByHabito(idHabito: Long): Int

    @Query("SELECT COUNT(*) FROM registro_habito WHERE idHabito = :idHabito AND estado = 1")
    suspend fun countCompletadosByHabito(idHabito: Long): Int
}
