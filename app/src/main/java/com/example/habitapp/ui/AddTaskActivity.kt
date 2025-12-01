package com.example.habitapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.Task
import com.example.habitapp.viewmodel.TasksViewModel
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: TasksViewModel
    private var editingTaskId: Long = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_add_task)

        // Configurar iconos de la status bar en color claro
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false

        // Configurar header
        val header = findViewById<View>(R.id.header_add_task)
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

        // Verificar si es modo edición
        editingTaskId = intent.getLongExtra("TASK_ID", -1L)
        isEditMode = editingTaskId != -1L

        headerTitle.text = if (isEditMode) "Editar Tarea" else "Nueva Tarea"
        headerSubtitle.text = if (isEditMode) "Modifica los datos de la tarea" else "Completa los datos de la tarea"

        viewModel = ViewModelProvider(this)[TasksViewModel::class.java]

        // Referencias a los campos
        val etTitle = findViewById<TextInputEditText>(R.id.et_task_title)
        val etDescription = findViewById<TextInputEditText>(R.id.et_task_description)
        val etDate = findViewById<TextInputEditText>(R.id.et_task_date)
        val rgPriority = findViewById<RadioGroup>(R.id.rg_priority)
        val btnSave = findViewById<Button>(R.id.btn_save_task)
        val btnCancel = findViewById<Button>(R.id.btn_cancel_task)

        // Hacer que el campo de fecha abra un DatePicker al tocarlo
        etDate.isFocusable = false
        etDate.isClickable = true
        etDate.setOnClickListener {
            showDatePicker(etDate.text?.toString())
        }

        // Si es modo edición, cargar los datos de la tarea
        if (isEditMode) {
            viewModel.tasks.observe(this) { tasks ->
                val task = tasks.find { it.id == editingTaskId }
                task?.let {
                    etTitle.setText(it.title)
                    etDescription.setText(it.subtitle)
                    etDate.setText(it.time)

                    when (it.priority) {
                        "Alta" -> rgPriority.check(R.id.rb_priority_high)
                        "Baja" -> rgPriority.check(R.id.rb_priority_low)
                        else -> rgPriority.check(R.id.rb_priority_medium)
                    }
                }
            }
            btnSave.text = "Actualizar Tarea"
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val date = etDate.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val priority = when (rgPriority.checkedRadioButtonId) {
                R.id.rb_priority_high -> "Alta"
                R.id.rb_priority_low -> "Baja"
                else -> "Media"
            }

            if (isEditMode) {
                // Actualizar tarea existente
                viewModel.updateTask(
                    editingTaskId,
                    title,
                    description,
                    date.ifEmpty { "Sin fecha" },
                    priority
                )
                Toast.makeText(this, "Tarea actualizada exitosamente", Toast.LENGTH_SHORT).show()
            } else {
                // Crear nueva tarea
                val newTask = Task(
                    id = System.currentTimeMillis(),
                    title = title,
                    subtitle = description,
                    time = date.ifEmpty { "Sin fecha" },
                    priority = priority,
                    done = false
                )
                viewModel.addTask(newTask)
                Toast.makeText(this, "Tarea creada exitosamente", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showDatePicker(initial: String?) {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("es", "ES"))
        val cal = Calendar.getInstance()
        try {
            if (!initial.isNullOrBlank()) {
                val d = sdf.parse(initial)
                if (d != null) cal.time = d
            }
        } catch (e: Exception) {
            // Ignorar y usar fecha actual
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            val selectedCal = Calendar.getInstance()
            selectedCal.set(y, m, d)
            val formatted = sdf.format(selectedCal.time)
            val etDate = findViewById<TextInputEditText>(R.id.et_task_date)
            etDate.setText(formatted)
        }, year, month, day)

        dpd.show()
    }
}
