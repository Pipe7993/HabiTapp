package com.example.habitapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "recordatorio",
    foreignKeys = [
        ForeignKey(
            entity = Tarea::class,
            parentColumns = ["idTarea"],
            childColumns = ["idTarea"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idTarea"], unique = true)])
data class Recordatorio(
    @PrimaryKey(autoGenerate = true)
    val idRecordatorio: Long = 0L,
    val fechaHora: Long,
    val activo: Boolean,
    val idTarea: Long
)
