package com.example.habitapp.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val header = view.findViewById<View>(R.id.header_settings)
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

        val tvNombreUsuario = view.findViewById<TextView>(R.id.tv_nombre_usuario)
        viewModel.nombreUsuario.observe(viewLifecycleOwner) { nombre ->
            tvNombreUsuario.text = nombre
        }
        viewModel.cargarNombreUsuario(1L)

        val btnEditarPerfil = view.findViewById<View>(R.id.btn_editar_perfil)
        btnEditarPerfil.setOnClickListener {
            val editText = EditText(requireContext())
            editText.hint = "Nuevo nombre"
            editText.setText(tvNombreUsuario.text)
            AlertDialog.Builder(requireContext())
                .setTitle("Editar nombre de perfil")
                .setView(editText)
                .setPositiveButton("Guardar") { _, _ ->
                    val nuevoNombre = editText.text.toString().trim()
                    viewModel.actualizarNombreUsuario(1L, nuevoNombre)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        return view
    }
}
