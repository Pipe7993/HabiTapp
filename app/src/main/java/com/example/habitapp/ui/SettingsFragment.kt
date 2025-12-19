package com.example.habitapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var viewModel: SettingsViewModel
    private var tempCameraUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let {
            requireContext().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.actualizarFotoUsuario(1L, it.toString())
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            tempCameraUri?.let { viewModel.actualizarFotoUsuario(1L, it.toString()) }
        }
    }

    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            launchCameraCapture()
        } else {
            Toast.makeText(requireContext(), "Se necesita permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

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
        val ivFoto = view.findViewById<android.widget.ImageView>(R.id.iv_foto_usuario)
        val btnQuitarFoto = view.findViewById<View>(R.id.btn_quitar_foto)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            swNot.isChecked = state.notifications
            swDark.isChecked = state.darkMode
            swRem.isChecked = state.reminders
        }

        viewModel.fotoUsuario.observe(viewLifecycleOwner) { uriString ->
            if (uriString.isNullOrBlank()) {
                ivFoto.setImageResource(R.drawable.ic_person)
            } else {
                ivFoto.setImageURI(Uri.parse(uriString))
            }
        }

        swNot.setOnCheckedChangeListener { _, isChecked -> viewModel.setNotifications(isChecked) }
        swDark.setOnCheckedChangeListener { _, isChecked -> viewModel.setDarkMode(isChecked) }
        swRem.setOnCheckedChangeListener { _, isChecked -> viewModel.setReminders(isChecked) }

        val tvNombreUsuario = view.findViewById<TextView>(R.id.tv_nombre_usuario)
        viewModel.nombreUsuario.observe(viewLifecycleOwner) { nombre ->
            tvNombreUsuario.text = nombre
        }
        viewModel.cargarUsuario(1L)

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

        val btnCambiarFoto = view.findViewById<View>(R.id.btn_cambiar_foto)
        btnCambiarFoto.setOnClickListener { showPhotoOptions() }

        btnQuitarFoto.setOnClickListener {
            viewModel.actualizarFotoUsuario(1L, null)
        }

        return view
    }

    private fun showPhotoOptions() {
        AlertDialog.Builder(requireContext())
            .setTitle("Seleccionar foto")
            .setItems(arrayOf("Tomar foto", "Elegir de galería")) { _, which ->
                when (which) {
                    0 -> requestCameraPermission.launch(android.Manifest.permission.CAMERA)
                    1 -> pickImageLauncher.launch(arrayOf("image/*"))
                }
            }
            .show()
    }

    private fun launchCameraCapture() {
        val uri = createTempImageUri()
        tempCameraUri = uri
        takePictureLauncher.launch(uri)
    }

    private fun createTempImageUri(): Uri {
        val imagesDir = File(requireContext().cacheDir, "images").apply { mkdirs() }
        val file = File.createTempFile("profile_", ".jpg", imagesDir)
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )
    }
}
