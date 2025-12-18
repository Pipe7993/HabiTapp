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
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.viewmodel.HabitoRoomViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

class HabitDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: HabitoRoomViewModel
    private var habitId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_habit_detail)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        val header = findViewById<View>(R.id.header_habit_detail)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.setPadding(v.paddingLeft, statusBarHeight + 24, v.paddingRight, v.paddingBottom)
            insets
        }

        // headerTitle.text = "Detalle de Hábito"
        // headerSubtitle.text = "Información completa"
        headerTitle.text = getString(R.string.habit_detail_title)
        headerSubtitle.text = getString(R.string.habit_detail_subtitle)

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[HabitoRoomViewModel::class.java]
        habitId = intent.getLongExtra("habit_id", -1L)

        val tvName = findViewById<TextView>(R.id.tv_detail_habit_name)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_habit_description)
        val tvStatus = findViewById<TextView>(R.id.tv_detail_habit_status)
        val tvFrequency = findViewById<TextView>(R.id.tv_detail_habit_frequency)
        val progressBar = findViewById<ProgressBar>(R.id.progress_detail_habit)
        val tvProgress = findViewById<TextView>(R.id.tv_detail_habit_progress)
        val btnEdit = findViewById<Button>(R.id.btn_edit_habit)
        val btnDelete = findViewById<Button>(R.id.btn_delete_habit)
        val btnBack = findViewById<Button>(R.id.btn_back_to_habits)

        var habitoActual: Habito? = null
        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.habitos.collectLatest { lista ->
                    if (lista.isEmpty()) return@collectLatest
                    val habit: Habito? = lista.firstOrNull { it.idHabito == habitId }
                    habitoActual = habit
                    if (habit != null) {
                        tvName.text = habit.titulo
                        tvDescription.text = habit.descripcion
                        tvStatus.text = if (habit.completado) getString(R.string.habit_status_completed) else getString(R.string.habit_status_pending)
                        tvFrequency.text = habit.tipo.name
                        progressBar.progress = if (habit.completado) 100 else 0
                        tvProgress.text = if (habit.completado) "100%" else "0%"
                    } else {
                        Toast.makeText(this@HabitDetailActivity, "Hábito no encontrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
        btnEdit.setOnClickListener {
            val intent = Intent(this, AddHabitActivity::class.java)
            intent.putExtra("habit_id", habitId)
            startActivity(intent)
        }
        btnDelete.setOnClickListener {
            habitoActual?.let {
                viewModel.delete(it)
                Toast.makeText(this, "Hábito eliminado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}
