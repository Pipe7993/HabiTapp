package com.example.habitapp.data.dao

import androidx.room.*
import com.example.habitapp.data.entity.EstadoTarea
import com.example.habitapp.data.entity.Prioridad
import com.example.habitapp.data.entity.Tarea
import com.example.habitapp.data.relation.TareaConRecordatorio
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tarea: Tarea): Long

    @Update
    suspend fun update(tarea: Tarea)

    @Delete
    suspend fun delete(tarea: Tarea)

    @Query("SELECT * FROM tarea WHERE idTarea = :id")
    suspend fun getById(id: Long): Tarea?

    @Query("SELECT * FROM tarea ORDER BY (fechaLimite IS NULL) ASC, fechaLimite ASC, prioridad DESC")
    fun getAll(): Flow<List<Tarea>>

    @Query("SELECT * FROM tarea WHERE idUsuario = :idUsuario ORDER BY prioridad DESC, (fechaLimite IS NULL) ASC, fechaLimite ASC")
    fun getTareasByUsuario(idUsuario: Long): Flow<List<Tarea>>

    @Transaction
    @Query("SELECT * FROM tarea WHERE idTarea = :id")
    fun getTareaConRecordatorio(id: Long): Flow<TareaConRecordatorio?>

    @Query("UPDATE tarea SET estado = :estado WHERE idTarea = :id")
    suspend fun cambiarEstado(id: Long, estado: EstadoTarea)

    @Query("SELECT * FROM tarea ORDER BY prioridad DESC, (fechaLimite IS NULL) ASC, fechaLimite ASC")
    fun getOrdenadasPorPrioridad(): Flow<List<Tarea>>

    @Query("SELECT * FROM tarea WHERE prioridad = :prioridad")
    fun getPorPrioridad(prioridad: Prioridad): Flow<List<Tarea>>

    @Query("SELECT * FROM tarea WHERE estado = :estado AND fechaLimite >= :inicio AND fechaLimite <= :fin")
    fun getCompletadasEnRango(estado: EstadoTarea, inicio: Long, fin: Long): Flow<List<Tarea>>

    @Query("SELECT * FROM tarea WHERE estado = :estado")
    fun getPorEstado(estado: EstadoTarea): Flow<List<Tarea>>

    @Query("SELECT * FROM tarea WHERE estado = :estado AND date(fechaLimite/1000, 'unixepoch', 'localtime') = date('now', 'localtime')")
    fun getParaHoy(estado: EstadoTarea): Flow<List<Tarea>>
}
