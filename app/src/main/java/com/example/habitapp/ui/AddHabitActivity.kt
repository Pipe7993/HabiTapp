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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.google.android.material.textfield.TextInputEditText

class AddHabitActivity : AppCompatActivity() {
    private lateinit var viewModel: HabitsViewModel
    private var editingHabitId: Int = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_add_habit)

        // Make the status bar use the same purple as the header
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_700)

        // Configurar iconos de la status bar en color claro
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        // Configurar header
        val header = findViewById<View>(R.id.header_add_habit)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            val extraTop = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(v.paddingLeft, statusBarHeight + extraTop, v.paddingRight, v.paddingBottom)
            insets
        }

        // Verificar modo edición
        editingHabitId = intent.getIntExtra("habit_id", -1)
        isEditMode = editingHabitId != -1

        headerTitle.text = if (isEditMode) getString(R.string.edit_habit_title) else getString(R.string.add_habit_title)
        headerSubtitle.text = if (isEditMode) getString(R.string.edit_habit_subtitle) else getString(R.string.add_habit_subtitle)

        viewModel = ViewModelProvider(this)[HabitsViewModel::class.java]

        val etName = findViewById<TextInputEditText>(R.id.et_habit_name)
        val etDescription = findViewById<TextInputEditText>(R.id.et_habit_description)
        val etFrequency = findViewById<TextInputEditText>(R.id.et_habit_frequency)
        val btnSave = findViewById<Button>(R.id.btn_save_habit)
        val btnCancel = findViewById<Button>(R.id.btn_cancel_habit)

        // Si es modo edición, cargar datos
        if (isEditMode) {
            viewModel.selectHabit(editingHabitId)
            viewModel.selectedHabit.observe(this) { habit ->
                habit?.let {
                    etName.setText(it.Nombre)
                    etDescription.setText(it.Descripcion)
                    etFrequency.setText(it.Frecuencia)
                }
            }
            btnSave.text = "Actualizar Hábito"
        }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val frequency = etFrequency.text.toString().trim().ifEmpty { "Diario" }

            if (name.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                viewModel.updateHabit(editingHabitId, name, description, frequency)
                Toast.makeText(this, "Hábito actualizado exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addHabit(name, description, frequency)
                Toast.makeText(this, "Hábito creado exitosamente", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
