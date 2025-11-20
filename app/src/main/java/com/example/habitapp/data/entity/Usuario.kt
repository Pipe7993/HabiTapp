package com.example.habitapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val idUsuario: Long = 0L,
    val nombre: String,
    val email: String
)

