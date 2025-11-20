package com.example.habitapp.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.data.entity.RegistroHabito

data class HabitoConRegistros(
    @Embedded val habito: Habito,
    @Relation(
        parentColumn = "idHabito",
        entityColumn = "idHabito"
    )
    val registros: List<RegistroHabito>
)
