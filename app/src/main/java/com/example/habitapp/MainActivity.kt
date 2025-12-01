package com.example.habitapp.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.habitapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragmentHabitos: Fragment = HabitsFragment()
    private val fragmentTareas: Fragment = TasksFragment()
    private val fragmentEstadisticas: Fragment = StatsFragment()
    private val fragmentAjustes: Fragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge-to-edge: permitir que el contenido ocupe toda la pantalla
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.activity_main)

        // Configurar iconos de la status bar en color claro (para que se vean sobre el fondo morado)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false // iconos claros

        // Ajustar insets dinámicamente para que la UI no quede tapada por navigation bar
        val container = findViewById<View>(R.id.fragment_contenedor)
        val barraNavegacion = findViewById<BottomNavigationView>(R.id.barra_navegacion)

        // El contenedor ya no necesita padding superior, solo dejar que los fragments manejen los insets
        ViewCompat.setOnApplyWindowInsetsListener(container) { v, insets ->
            // No aplicar padding, dejar que cada fragment maneje su header
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(barraNavegacion) { v, insets ->
            val navBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            // Asegurar padding inferior para la barra de navegación
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, navBars.bottom)
            insets
        }

        // Mostrar fragmento por defecto
        mostrarFragmento(fragmentHabitos)

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
