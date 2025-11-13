package com.example.habitapp.data.dao

import androidx.room.*
import com.example.habitapp.data.entity.Recordatorio
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordatorioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordatorio: Recordatorio): Long

    @Update
    suspend fun update(recordatorio: Recordatorio)

    @Delete
    suspend fun delete(recordatorio: Recordatorio)

    @Query("SELECT * FROM recordatorio WHERE idRecordatorio = :id")
    suspend fun getById(id: Long): Recordatorio?

    @Query("SELECT * FROM recordatorio ORDER BY fechaHora ASC")
    fun getAll(): Flow<List<Recordatorio>>

    @Query("SELECT * FROM recordatorio WHERE idTarea = :idTarea LIMIT 1")
    suspend fun getByTarea(idTarea: Long): Recordatorio?
}
