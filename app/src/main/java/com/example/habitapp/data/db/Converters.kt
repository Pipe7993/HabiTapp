package com.example.habitapp.data.db

import androidx.room.TypeConverter
import com.example.habitapp.data.entity.EstadoTarea
import com.example.habitapp.data.entity.Prioridad
import com.example.habitapp.data.entity.TipoHabito

class Converters {
    @TypeConverter
    fun fromTipoHabito(value: TipoHabito?): String? = value?.name

    @TypeConverter
    fun toTipoHabito(value: String?): TipoHabito? = value?.let { TipoHabito.valueOf(it) }

    @TypeConverter
    fun fromPrioridad(value: Prioridad?): String? = value?.name

    @TypeConverter
    fun toPrioridad(value: String?): Prioridad? = value?.let { Prioridad.valueOf(it) }

    @TypeConverter
    fun fromEstadoTarea(value: EstadoTarea?): String? = value?.name

    @TypeConverter
    fun toEstadoTarea(value: String?): EstadoTarea? = value?.let { EstadoTarea.valueOf(it) }
}

