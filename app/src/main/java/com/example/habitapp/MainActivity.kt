package com.example.habitapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(R.layout.activity_main)

        // Mostrar fragmento por defecto
        mostrarFragmento(fragmentHabitos)

        val barraNavegacion = findViewById<BottomNavigationView>(R.id.barra_navegacion)
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
