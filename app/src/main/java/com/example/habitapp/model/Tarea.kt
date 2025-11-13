package com.example.habitapp.model

import java.util.Date

data class Tarea(
    val id: Int,
    val Titulo: String,
    val Descripcion: String,
    val FechaLimite: Date,
    val Prioridad: Int,
    val Estado: String,
    val completado: Boolean = false
)
