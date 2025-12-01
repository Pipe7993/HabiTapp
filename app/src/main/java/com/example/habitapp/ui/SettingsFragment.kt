package com.example.habitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Configurar header
        val header = view.findViewById<View>(R.id.header_settings)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        // Ajustar padding superior del header para que cubra la status bar
        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            v.setPadding(
                v.paddingLeft,
                statusBarHeight + 24, // 24dp adicionales después de la status bar
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        headerTitle.text = "Configuración"
        headerSubtitle.text = "Ajustes de la aplicación"

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
