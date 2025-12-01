package com.example.habitapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.TasksViewModel

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: TasksViewModel
    private var taskId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_task_detail)

        // Configurar iconos de la status bar en color claro
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        // Configurar header
        val header = findViewById<View>(R.id.header_task_detail)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        // Ajustar padding del header
        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.setPadding(
                v.paddingLeft,
                statusBarHeight + 24,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        headerTitle.text = "Detalle de Tarea"
        headerSubtitle.text = "Información completa"

        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        // Obtener ID de la tarea desde el Intent
        taskId = intent.getLongExtra("TASK_ID", -1)

        // Referencias a las vistas
        val tvTitle = findViewById<TextView>(R.id.tv_detail_task_title)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_task_description)
        val tvTime = findViewById<TextView>(R.id.tv_detail_task_time)
        val tvPriority = findViewById<TextView>(R.id.tv_detail_task_priority)
        val btnEdit = findViewById<Button>(R.id.btn_edit_task)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)
        val btnBack = findViewById<Button>(R.id.btn_back_to_tasks)

        // Observar las tareas y buscar la actual
        viewModel.tasks.observe(this) { tasks ->
            val task = tasks.find { it.id == taskId }
            if (task != null) {
                tvTitle.text = task.title
                tvDescription.text = task.subtitle
                tvTime.text = task.time
                tvPriority.text = task.priority

                // Aplicar color según prioridad
                when (task.priority.lowercase()) {
                    "alta" -> tvPriority.setBackgroundResource(R.drawable.bg_tag_red)
                    "media" -> tvPriority.setBackgroundResource(R.drawable.bg_tag_orange)
                    "baja" -> tvPriority.setBackgroundResource(R.drawable.bg_tag_blue)
                }
            } else {
                Toast.makeText(this, "Tarea no encontrada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnEdit.setOnClickListener {
            val intent = android.content.Intent(this, AddTaskActivity::class.java)
            intent.putExtra("TASK_ID", taskId)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Tarea")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Eliminar") { _, _ ->
                    viewModel.removeTask(taskId)
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
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

