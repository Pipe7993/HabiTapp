package com.example.habitapp.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.data.entity.Tarea
import com.example.habitapp.data.entity.Usuario

data class UsuarioConHabitosYTareas(
    @Embedded val usuario: Usuario,
    @Relation(
        parentColumn = "idUsuario",
        entityColumn = "idUsuario"
    )
    val habitos: List<Habito>,
    @Relation(
        parentColumn = "idUsuario",
        entityColumn = "idUsuario"
    )
    val tareas: List<Tarea>
)
