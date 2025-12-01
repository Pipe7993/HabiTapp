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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.R
import com.example.habitapp.viewmodel.HabitsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HabitsFragment : Fragment() {
    private lateinit var adapter: HabitsAdapter
    private lateinit var viewModel: HabitsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_habits, container, false)

        // Configurar header
        val header = view.findViewById<View>(R.id.header_habits)
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

        headerTitle.text = "Mis Hábitos"
        // Obtener número de hábitos del ViewModel cuando esté disponible
        headerSubtitle.text = "4 hábitos activos"

        viewModel = ViewModelProvider(requireActivity())[HabitsViewModel::class.java]

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_habits)
        adapter = HabitsAdapter { item ->
            // Placeholder: responder click en hábito (por ejemplo, abrir detalle)
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // FAB para agregar hábito
        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_habit)
        fab.setOnClickListener {
            // TODO: Abrir diálogo o actividad para agregar hábito
        }

        // Observar LiveData del ViewModel
        viewModel.habits.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            // Actualizar subtítulo del header con el número de hábitos
            headerSubtitle.text = "${list.size} hábitos activos"
        }

        return view
    }
}
