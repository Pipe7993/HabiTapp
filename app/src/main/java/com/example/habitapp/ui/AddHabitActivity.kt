package com.example.habitapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.data.entity.Usuario
import com.example.habitapp.data.db.AppDatabase
import com.example.habitapp.viewmodel.HabitoRoomViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddHabitActivity : AppCompatActivity() {
    private lateinit var viewModel: HabitoRoomViewModel

    private var usuarioId: Long = 0L
    private var habitoEdit: Habito? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_add_habit)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        val header = findViewById<View>(R.id.header_add_habit)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.setPadding(v.paddingLeft, statusBarHeight + 24, v.paddingRight, v.paddingBottom)
            insets
        }

        // Verificar modo edición
        // editingHabitId = intent.getIntExtra("habit_id", -1)
        // isEditMode = editingHabitId != -1
        // headerTitle.text = if (isEditMode) "Editar Hábito" else "Nuevo Hábito"
        // headerSubtitle.text = if (isEditMode) "Modifica los datos del hábito" else "Completa los datos del hábito"
        headerTitle.text = getString(R.string.add_habit_title)
        headerSubtitle.text = getString(R.string.add_habit_subtitle)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[HabitoRoomViewModel::class.java]

        usuarioId = runBlocking(Dispatchers.IO) {
            val db = AppDatabase.getInstance(application)
            val usuarioDao = db.usuarioDao()
            val usuarios = usuarioDao.getAll().first()
            if (usuarios.isEmpty()) {
                usuarioDao.insert(Usuario(nombre = "Usuario", email = "usuario@demo.com"))
            }
            usuarioDao.getAll().first().firstOrNull()?.idUsuario ?: 1L
        }

        val etName = findViewById<TextInputEditText>(R.id.et_habit_name)
        val etDescription = findViewById<TextInputEditText>(R.id.et_habit_description)
        val btnSave = findViewById<Button>(R.id.btn_save_habit)
        val btnCancel = findViewById<Button>(R.id.btn_cancel_habit)

        val habitId = intent.getLongExtra("habit_id", -1L)
        if (habitId != -1L) {
            // Modo edición: cargar datos
            runBlocking(Dispatchers.IO) {
                val db = AppDatabase.getInstance(application)
                val habitoDao = db.habitoDao()
                habitoEdit = habitoDao.getById(habitId)
            }
            habitoEdit?.let {
                etName.setText(it.titulo)
                etDescription.setText(it.descripcion)
                headerTitle.text = getString(R.string.edit_habit_title)
                headerSubtitle.text = getString(R.string.edit_habit_subtitle)
            }
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (habitoEdit != null) {
                // Actualizar
                val habitoActualizado = habitoEdit!!.copy(
                    titulo = name,
                    descripcion = description
                )
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.update(habitoActualizado)
                    runOnUiThread {
                        Toast.makeText(this@AddHabitActivity, "Hábito actualizado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                // Crear nuevo
                val habito = Habito(
                    titulo = name,
                    descripcion = description,
                    tipo = com.example.habitapp.data.entity.TipoHabito.DIARIO,
                    completado = false,
                    idUsuario = usuarioId
                )
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertAndReturnId(habito)
                    runOnUiThread {
                        Toast.makeText(this@AddHabitActivity, "Hábito creado exitosamente", Toast.LENGTH_SHORT).show()
                        // Redirigir a la vista principal de hábitos
                        val intent = android.content.Intent(this@AddHabitActivity, MainActivity::class.java)
                        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
