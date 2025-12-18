package com.example.habitapp.ui

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.data.entity.EstadoTarea
import com.example.habitapp.data.entity.Prioridad
import com.example.habitapp.data.entity.Tarea
import com.example.habitapp.viewmodel.TareaRoomViewModel
import com.example.habitapp.viewmodel.TasksViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.example.habitapp.worker.TaskNotificationWorker

class AddTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: TasksViewModel
    private lateinit var tareaViewModel: TareaRoomViewModel
    private var editingTaskId: Long = -1
    private var isEditMode = false
    private var usuarioId: Long = 1L
    private var tareaEdit: com.example.habitapp.data.entity.Tarea? = null

    private fun solicitarPermisoNotificacionesSiEsNecesario() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                    Toast.makeText(this, "Para recibir recordatorios de tareas, activa el permiso de notificaciones en ajustes.", Toast.LENGTH_LONG).show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        solicitarPermisoNotificacionesSiEsNecesario()

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_add_task)

        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        val header = findViewById<View>(R.id.header_add_task)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

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

        editingTaskId = intent.getLongExtra("TASK_ID", -1L)
        isEditMode = editingTaskId != -1L

        headerTitle.text = if (isEditMode) "Editar Tarea" else "Nueva Tarea"
        headerSubtitle.text = if (isEditMode) "Modifica los datos de la tarea" else "Completa los datos de la tarea"

        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]
        tareaViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[TareaRoomViewModel::class.java]

        val etTitle = findViewById<TextInputEditText>(R.id.et_task_title)
        val etDescription = findViewById<TextInputEditText>(R.id.et_task_description)
        val etDate = findViewById<TextInputEditText>(R.id.et_task_date)
        val rgPriority = findViewById<RadioGroup>(R.id.rg_priority)
        val btnSave = findViewById<Button>(R.id.btn_save_task)
        val btnCancel = findViewById<Button>(R.id.btn_cancel_task)

        etDate.isFocusable = false
        etDate.isClickable = true
        etDate.setOnClickListener {
            showDateTimePicker(etDate)
        }

        if (isEditMode) {
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                tareaEdit = tareaViewModel.getById(editingTaskId)
                tareaEdit?.let {
                    etTitle.setText(it.titulo)
                    etDescription.setText(it.descripcion)
                    etDate.setText(it.fechaLimite?.let { fecha -> java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(fecha)) } ?: "")
                    when (it.prioridad) {
                        Prioridad.ALTA -> rgPriority.check(R.id.rb_priority_high)
                        Prioridad.BAJA -> rgPriority.check(R.id.rb_priority_low)
                        Prioridad.MEDIA -> rgPriority.check(R.id.rb_priority_medium)
                    }
                    btnSave.text = getString(R.string.edit_task_title) // Usa string resource
                }
            }
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val dateStr = etDate.text.toString().trim()
            val fechaLimite = if (dateStr.isNotEmpty()) {
                try {
                    java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateStr)?.time
                } catch (_: Exception) { null }
            } else null
            val prioridad = when (rgPriority.checkedRadioButtonId) {
                R.id.rb_priority_high -> Prioridad.ALTA
                R.id.rb_priority_medium -> Prioridad.MEDIA
                R.id.rb_priority_low -> Prioridad.BAJA
                else -> Prioridad.MEDIA
            }
            if (title.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (tareaEdit != null) {
                // Actualizar
                val tareaActualizada = tareaEdit!!.copy(
                    titulo = title,
                    descripcion = description,
                    fechaLimite = fechaLimite,
                    prioridad = prioridad
                )
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    tareaViewModel.update(tareaActualizada)
                    // Programar notificación si hay fecha
                    if (fechaLimite != null) {
                        programarNotificacionTarea(tareaActualizada.idTarea, title, description, fechaLimite)
                    }
                    runOnUiThread {
                        Toast.makeText(this@AddTaskActivity, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                val tarea = com.example.habitapp.data.entity.Tarea(
                    titulo = title,
                    descripcion = description,
                    fechaLimite = fechaLimite,
                    prioridad = prioridad,
                    estado = EstadoTarea.PENDIENTE,
                    idUsuario = usuarioId
                )
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    val id = tareaViewModel.insertAndReturnId(tarea)
                    if (fechaLimite != null) {
                        programarNotificacionTarea(id, title, description, fechaLimite)
                    }
                    runOnUiThread {
                        Toast.makeText(this@AddTaskActivity, "Tarea creada exitosamente", Toast.LENGTH_SHORT).show()
                        val intent = android.content.Intent(this@AddTaskActivity, MainActivity::class.java)
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

    private fun showDateTimePicker(etDate: TextInputEditText) {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "ES"))
        try {
            val text = etDate.text?.toString()
            if (!text.isNullOrBlank()) {
                val d = sdf.parse(text)
                if (d != null) cal.time = d
            }
        } catch (_: Exception) {}
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val dpd = android.app.DatePickerDialog(this, { _, y, m, d ->
            val tpd = android.app.TimePickerDialog(this, { _, h, min ->
                val selectedCal = Calendar.getInstance()
                selectedCal.set(y, m, d, h, min)
                val formatted = sdf.format(selectedCal.time)
                etDate.setText(formatted)
            }, hour, minute, true)
            tpd.show()
        }, year, month, day)
        dpd.show()
    }

    private fun programarNotificacionTarea(taskId: Long, title: String, description: String, fechaLimite: Long) {
        val delay = fechaLimite - System.currentTimeMillis() - 60 * 60 * 1000 // 1 hora antes
        if (delay > 0) {
            val data = Data.Builder()
                .putString("title", title)
                .putString("description", description)
                .putLong("taskId", taskId)
                .build()
            val request = OneTimeWorkRequestBuilder<TaskNotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .build()
            WorkManager.getInstance(this).enqueue(request)
        }
    }

    // Puedes manejar la respuesta del usuario si lo deseas:
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No podrás recibir notificaciones de tareas. Puedes activarlo en Ajustes > Aplicaciones.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
