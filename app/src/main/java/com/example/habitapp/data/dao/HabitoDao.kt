package com.example.habitapp.data.dao

import androidx.room.*
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.data.entity.TipoHabito
import com.example.habitapp.data.relation.HabitoConRegistros
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habito: Habito): Long

    @Update
    suspend fun update(habito: Habito)

    @Delete
    suspend fun delete(habito: Habito)

    @Query("SELECT * FROM habito WHERE idHabito = :id")
    suspend fun getById(id: Long): Habito?

    @Query("SELECT * FROM habito ORDER BY titulo ASC")
    fun getAll(): Flow<List<Habito>>

    @Query("SELECT * FROM habito WHERE idUsuario = :idUsuario ORDER BY titulo ASC")
    fun getHabitosByUsuario(idUsuario: Long): Flow<List<Habito>>

    @Transaction
    @Query("SELECT * FROM habito WHERE idHabito = :id")
    fun getHabitoConRegistros(id: Long): Flow<HabitoConRegistros?>

    @Query("UPDATE habito SET completado = :completado WHERE idHabito = :id")
    suspend fun marcarCompletado(id: Long, completado: Boolean)

    @Query("SELECT * FROM habito WHERE tipo = :tipo")
    fun getHabitosPorTipo(tipo: TipoHabito): Flow<List<Habito>>

    @Query("SELECT * FROM habito WHERE completado = 1 AND fechaCreacion >= :inicio AND fechaCreacion <= :fin")
    fun getCompletadosEnRango(inicio: Long, fin: Long): Flow<List<Habito>>

    @Query("SELECT * FROM habito WHERE completado = 0")
    fun getActivos(): Flow<List<Habito>>
}
