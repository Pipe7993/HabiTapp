package com.example.habitapp.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habitapp.data.entity.Recordatorio
import com.example.habitapp.data.entity.Tarea

data class TareaConRecordatorio(
    @Embedded val tarea: Tarea,
    @Relation(
        parentColumn = "idTarea",
        entityColumn = "idTarea"
    )
    val recordatorio: Recordatorio?
)
