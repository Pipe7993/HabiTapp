package com.example.habitapp.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registro_habito",
    foreignKeys = [
        ForeignKey(
            entity = Habito::class,
            parentColumns = ["idHabito"],
            childColumns = ["idHabito"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["idHabito"])])
data class RegistroHabito(
    @PrimaryKey(autoGenerate = true)
    val idRegistro: Long = 0L,
    val fecha: Long,
    val estado: Boolean,
    val idHabito: Long
)

