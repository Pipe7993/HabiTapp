package com.example.habitapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.habitapp.R
import com.google.android.material.snackbar.Snackbar

class AddHabitActivity : AppCompatActivity() {
    private val viewModel: HabitsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        val nameInput = findViewById<EditText>(R.id.input_habit_name)
        val descInput = findViewById<EditText>(R.id.input_habit_desc)
        val saveBtn = findViewById<Button>(R.id.btn_save_habit)

        viewModel.addState.observe(this, Observer { state ->
            if (state.error != null) {
                Toast.makeText(this, state.error, Toast.LENGTH_SHORT).show()
            } else if (state.success) {
                val rootView = window.decorView.findViewById(android.R.id.content) as android.view.View
                val snackbar = Snackbar.make(rootView, getString(R.string.habit_added_popup), Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction(getString(R.string.close)) {
                    snackbar.dismiss()
                    finish()
                }
                snackbar.setDuration(10_000)
                snackbar.show()
                Handler(Looper.getMainLooper()).postDelayed({
                    snackbar.dismiss()
                    finish()
                }, 10_000)
            }
        })

        saveBtn.setOnClickListener {
            val name = nameInput.text?.toString()?.trim().orEmpty()
            val desc = descInput.text?.toString()?.trim().orEmpty()
            viewModel.addHabit(name, desc)
        }
    }
}
