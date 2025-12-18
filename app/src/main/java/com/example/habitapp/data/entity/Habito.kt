package com.example.habitapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habito",
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
data class Habito(
    @PrimaryKey(autoGenerate = true)
    val idHabito: Long = 0L,
    val titulo: String,
    val descripcion: String,
    val tipo: TipoHabito,
    val completado: Boolean = false,
    val idUsuario: Long,
    val fechaCreacion: Long = System.currentTimeMillis()
)

enum class TipoHabito { DIARIO, SEMANAL }
