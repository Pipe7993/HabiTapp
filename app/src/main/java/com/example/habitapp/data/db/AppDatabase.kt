package com.example.habitapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habitapp.data.dao.*
import com.example.habitapp.data.entity.*

@Database(
    entities = [Usuario::class, Habito::class, RegistroHabito::class, Tarea::class, Recordatorio::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun habitoDao(): HabitoDao
    abstract fun registroHabitoDao(): RegistroHabitoDao
    abstract fun tareaDao(): TareaDao
    abstract fun recordatorioDao(): RecordatorioDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "habitapp.db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}

