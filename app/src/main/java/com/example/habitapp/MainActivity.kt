package com.example.habitapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.habitapp.R
import com.example.habitapp.data.db.AppDatabase
import com.example.habitapp.viewmodel.DaoProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragmentHabitos: Fragment = HabitsFragment()
    private val fragmentTareas: Fragment = TasksFragment()
    private val fragmentEstadisticas: Fragment = StatsFragment()
    private val fragmentAjustes: Fragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el UsuarioDao para el ViewModel
        val db = AppDatabase.getInstance(applicationContext)
        DaoProvider.usuarioDao = db.usuarioDao()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
        }

        val container = findViewById<View>(R.id.fragment_contenedor)
        val barraNavegacion = findViewById<BottomNavigationView>(R.id.barra_navegacion)

        ViewCompat.setOnApplyWindowInsetsListener(container) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(0, 0, 0, navBars.bottom)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(barraNavegacion) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, navBars.bottom)
            insets
        }

        mostrarFragmento(fragmentEstadisticas)

        barraNavegacion.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_habitos -> mostrarFragmento(fragmentHabitos)
                R.id.nav_tareas -> mostrarFragmento(fragmentTareas)
                R.id.nav_estadisticas -> mostrarFragmento(fragmentEstadisticas)
                R.id.nav_ajustes -> mostrarFragmento(fragmentAjustes)
            }
            true
        }
    }

    private fun mostrarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_contenedor, fragmento)
            .commit()
    }
}
