package com.example.habitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        val swNot = view.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.switch_notifications)
        val swDark = view.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.switch_darkmode)
        val swRem = view.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.switch_reminders)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            swNot.isChecked = state.notifications
            swDark.isChecked = state.darkMode
            swRem.isChecked = state.reminders
        }

        swNot.setOnCheckedChangeListener { _, isChecked -> viewModel.setNotifications(isChecked) }
        swDark.setOnCheckedChangeListener { _, isChecked -> viewModel.setDarkMode(isChecked) }
        swRem.setOnCheckedChangeListener { _, isChecked -> viewModel.setReminders(isChecked) }

        return view
    }
}
