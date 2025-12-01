package com.example.habitapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.habitapp.R

class HabitDetailActivity : AppCompatActivity() {
    private val viewModel: HabitsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_detail)

        val nameInput = findViewById<EditText>(R.id.input_habit_name_detail)
        val descInput = findViewById<EditText>(R.id.input_habit_desc_detail)
        val saveBtn = findViewById<Button>(R.id.btn_update_habit)
        val openSettingsBtn = findViewById<Button>(R.id.btn_open_settings)

        val habitId = intent.getIntExtra("habit_id", -1)
        viewModel.selectHabit(habitId)

        viewModel.selectedHabit.observe(this, Observer { habit ->
            if (habit != null) {
                nameInput.setText(habit.Nombre)
                descInput.setText(habit.Descripcion)
            }
        })

        saveBtn.setOnClickListener {
            viewModel.updateHabit(
                nameInput.text?.toString()?.trim().orEmpty(),
                descInput.text?.toString()?.trim().orEmpty()
            )
            finish()
        }

        openSettingsBtn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
