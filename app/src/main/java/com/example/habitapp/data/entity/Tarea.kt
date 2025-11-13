package com.example.habitapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tarea",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["idUsuario"],
            childColumns = ["idUsuario"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idUsuario"])])
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val idTarea: Long = 0L,
    val titulo: String,
    val descripcion: String,
    val fechaLimite: Long?,
    val prioridad: Prioridad,
    val estado: EstadoTarea,
    val idUsuario: Long
)

enum class Prioridad { BAJA, MEDIA, ALTA }
enum class EstadoTarea { PENDIENTE, EN_PROGRESO, COMPLETADA }
