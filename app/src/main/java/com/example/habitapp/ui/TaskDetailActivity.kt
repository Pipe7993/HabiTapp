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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.data.entity.Tarea
import com.example.habitapp.data.entity.Prioridad
import com.example.habitapp.viewmodel.TareaRoomViewModel
import com.example.habitapp.viewmodel.TasksViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: TasksViewModel
    private lateinit var tareaViewModel: TareaRoomViewModel
    private var taskId: Long = -1
    private var tareaActual: Tarea? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_task_detail)

        // Make the status bar use the same purple as the header so the header appears to reach the top
        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_700)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        val header = findViewById<View>(R.id.header_task_detail)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            // Convert 24dp to pixels to avoid mixing dp and px
            val extraTop = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(
                v.paddingLeft,
                statusBarHeight + extraTop,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        // headerTitle.text = "Detalle de Tarea"
        // headerSubtitle.text = "Información completa"
        headerTitle.text = getString(R.string.task_detail_title)
        headerSubtitle.text = getString(R.string.task_detail_subtitle)

        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        tareaViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TareaRoomViewModel::class.java]
        val tareaId = intent.getLongExtra("TASK_ID", -1L)

        val tvTitle = findViewById<TextView>(R.id.tv_detail_task_title)
        val tvDescription = findViewById<TextView>(R.id.tv_detail_task_description)
        val tvTime = findViewById<TextView>(R.id.tv_detail_task_time)
        val tvPriority = findViewById<TextView>(R.id.tv_detail_task_priority)
        val btnEdit = findViewById<Button>(R.id.btn_edit_task)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)
        val btnBack = findViewById<Button>(R.id.btn_back_to_tasks)
        val btnComplete = findViewById<Button>(R.id.btn_complete_task)

        lifecycleScope.launch {
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                tareaViewModel.tareas.collectLatest { lista ->
                    if (lista.isEmpty()) return@collectLatest
                    val tarea = lista.firstOrNull { it.idTarea == tareaId }
                    tareaActual = tarea
                    if (tarea != null) {
                        tvTitle.text = tarea.titulo
                        tvDescription.text = tarea.descripcion
                        tvTime.text = tarea.fechaLimite?.let { java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(it)) } ?: "Sin fecha"
                        tvPriority.text = when (tarea.prioridad) {
                            Prioridad.ALTA -> "Alta"
                            Prioridad.MEDIA -> "Media"
                            Prioridad.BAJA -> "Baja"
                        }
                        when (tarea.prioridad) {
                            Prioridad.ALTA -> tvPriority.setBackgroundResource(R.drawable.bg_tag_red)
                            Prioridad.MEDIA -> tvPriority.setBackgroundResource(R.drawable.bg_tag_orange)
                            Prioridad.BAJA -> tvPriority.setBackgroundResource(R.drawable.bg_tag_blue)
                        }
                        btnComplete.text = if (tarea.estado == com.example.habitapp.data.entity.EstadoTarea.COMPLETADA) "Marcar como pendiente" else "Marcar como completada"
                    } else {
                        Toast.makeText(this@TaskDetailActivity, "Tarea no encontrada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
        btnComplete.setOnClickListener {
            tareaActual?.let { tarea ->
                val nuevoEstado = if (tarea.estado == com.example.habitapp.data.entity.EstadoTarea.COMPLETADA)
                    com.example.habitapp.data.entity.EstadoTarea.PENDIENTE
                else
                    com.example.habitapp.data.entity.EstadoTarea.COMPLETADA
                tareaViewModel.cambiarEstado(tarea.idTarea, nuevoEstado)
            }
        }
        btnEdit.setOnClickListener {
            val intent = android.content.Intent(this, AddTaskActivity::class.java)
            intent.putExtra("TASK_ID", tareaId)
            startActivity(intent)
        }
        btnDelete.setOnClickListener {
            tareaActual?.let {
                tareaViewModel.delete(it)
                Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
