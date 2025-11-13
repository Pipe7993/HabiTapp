package com.example.habitapp.data.db

import com.example.habitapp.data.dao.HabitoDao
import com.example.habitapp.data.dao.RecordatorioDao
import com.example.habitapp.data.dao.RegistroHabitoDao
import com.example.habitapp.data.dao.TareaDao
import com.example.habitapp.data.entity.*

class DomainMethods(
    private val habitoDao: HabitoDao,
    private val registroDao: RegistroHabitoDao,
    private val tareaDao: TareaDao,
    private val recordatorioDao: RecordatorioDao
) {
    suspend fun marcarHabitoCompletado(idHabito: Long, completado: Boolean) {
        habitoDao.marcarCompletado(idHabito, completado)
    }

    suspend fun registrarAvanceHabito(idHabito: Long, fechaMillis: Long, estado: Boolean): Long {
        return registroDao.insert(
            RegistroHabito(
                fecha = fechaMillis,
                estado = estado,
                idHabito = idHabito
            )
        )
    }

    suspend fun cambiarEstadoTarea(idTarea: Long, estado: EstadoTarea) {
        tareaDao.cambiarEstado(idTarea, estado)
    }

    suspend fun obtenerPorcentajeAvanceHabito(idHabito: Long): Double {
        val total = registroDao.countByHabito(idHabito)
        if (total == 0) return 0.0
        val completados = registroDao.countCompletadosByHabito(idHabito)
        return completados.toDouble() * 100.0 / total.toDouble()
    }

    fun obtenerTareasOrdenadasPorPrioridad() = tareaDao.getOrdenadasPorPrioridad()

    fun obtenerHabitosDiarios() = habitoDao.getHabitosPorTipo(TipoHabito.DIARIO)
    fun obtenerHabitosSemanales() = habitoDao.getHabitosPorTipo(TipoHabito.SEMANAL)

    suspend fun crearRecordatorioParaTarea(idTarea: Long, fechaHora: Long, activo: Boolean): Long {
        val recordatorio = Recordatorio(
            fechaHora = fechaHora,
            activo = activo,
            idTarea = idTarea
        )
        return recordatorioDao.insert(recordatorio)
    }
}
