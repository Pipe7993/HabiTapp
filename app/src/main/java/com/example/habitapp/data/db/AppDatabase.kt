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
    version = 3,
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
            )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .fallbackToDestructiveMigration()
            .build().also { INSTANCE = it }
        }

        // MIGRACIÓN: Añadir columna fechaCreacion a habito
        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE habito ADD COLUMN fechaCreacion INTEGER NOT NULL DEFAULT 0")
            }
        }

        // MIGRACIÓN: Añadir columna fotoUri a usuario
        private val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE usuario ADD COLUMN fotoUri TEXT")
            }
        }
    }
}
