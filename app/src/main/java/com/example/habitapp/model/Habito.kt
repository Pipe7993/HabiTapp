package com.example.habitapp.model

data class Habito(
    val id: Int,
    val Nombre: String,
    val Descripcion: String,
    val Frecuencia: String,
    val Progreso: Int,
    val Estado: String,
    val Activo: Boolean = true
)
