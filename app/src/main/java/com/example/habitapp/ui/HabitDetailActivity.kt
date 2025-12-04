package com.example.habitapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R

class HabitDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: HabitsViewModel
    private var habitId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_habit_detail)

        // Make the status bar use the same purple as the header
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_700)

        // Configurar iconos de la status bar en color claro
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        // Configurar header
        val header = findViewById<View>(R.id.header_habit_detail)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            val extraTop = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(v.paddingLeft, statusBarHeight + extraTop, v.paddingRight, v.paddingBottom)
            insets
        }

        headerTitle.text = getString(R.string.habit_detail_title)
        headerSubtitle.text = getString(R.string.habit_detail_subtitle)

        viewModel = ViewModelProvider(this)[HabitsViewModel::class.java]

        habitId = intent.getIntExtra("habit_id", -1)

        val tvName = findViewById<TextView>(R.id.tv_detail_habit_name)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_habit_description)
        val tvStatus = findViewById<TextView>(R.id.tv_detail_habit_status)
        val tvFrequency = findViewById<TextView>(R.id.tv_detail_habit_frequency)
        val progressBar = findViewById<ProgressBar>(R.id.progress_detail_habit)
        val tvProgress = findViewById<TextView>(R.id.tv_detail_habit_progress)
        val btnEdit = findViewById<Button>(R.id.btn_edit_habit)
        val btnDelete = findViewById<Button>(R.id.btn_delete_habit)
        val btnBack = findViewById<Button>(R.id.btn_back_to_habits)

        // Observar hábitos y buscar el actual
        viewModel.habits.observe(this) { habits ->
            val habit = habits.find { it.id == habitId }
            if (habit != null) {
                tvName.text = habit.Nombre
                tvDescription.text = habit.Descripcion
                tvStatus.text = if (habit.Activo) "Activo" else "Inactivo"
                tvFrequency.text = habit.Frecuencia
                progressBar.progress = habit.Progreso
                tvProgress.text = "${habit.Progreso}%"
            } else {
                Toast.makeText(this, "Hábito no encontrado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this, AddHabitActivity::class.java)
            intent.putExtra("habit_id", habitId)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Hábito")
                .setMessage("¿Estás seguro de que deseas eliminar este hábito?")
                .setPositiveButton("Eliminar") { _, _ ->
                    viewModel.deleteHabit(habitId)
                    Toast.makeText(this, "Hábito eliminado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
