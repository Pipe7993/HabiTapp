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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.R
import com.example.habitapp.viewmodel.StatsViewModel
import com.example.habitapp.viewmodel.TareaRoomViewModel
import com.example.habitapp.viewmodel.HabitoRoomViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StatsFragment : Fragment() {
    private lateinit var viewModel: StatsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        val header = view.findViewById<View>(R.id.header_stats)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            // Convert 24dp to pixels
            val extraTop = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(
                v.paddingLeft,
                statusBarHeight + extraTop,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        headerTitle.text = "Estadísticas"
        headerSubtitle.text = "Tu progreso"

        viewModel = ViewModelProvider(requireActivity())[StatsViewModel::class.java]


        // Tareas completadas y pendientes
        val tvCompleted = view.findViewById<TextView>(R.id.tv_completed_tasks_count)
        val tvPending = view.findViewById<TextView>(R.id.tv_pending_tasks_count)
        val tareaViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[TareaRoomViewModel::class.java]
        lifecycleScope.launch {
            tareaViewModel.getCompletadasUltimoMes().collectLatest { lista ->
                tvCompleted.text = lista.size.toString()
            }
        }
        lifecycleScope.launch {
            tareaViewModel.getPendientes().collectLatest { lista ->
                tvPending.text = lista.size.toString()
            }
        }
        // Tareas para hoy
        val rvTasksToday = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_tasks_today_stats)
        val tasksTodayAdapter = TasksAdapter { tarea ->
            // Aqui poner para redirigir a detalles
        }
        rvTasksToday.layoutManager = LinearLayoutManager(requireContext())
        rvTasksToday.adapter = tasksTodayAdapter
        lifecycleScope.launch {
            tareaViewModel.getParaHoy().collectLatest { lista ->
                tasksTodayAdapter.submitList(lista)
            }
        }

        // Hábitos completados y activos
        val tvCompletedHabits = view.findViewById<TextView>(R.id.tv_completed_habits_count)
        val tvActiveHabits = view.findViewById<TextView>(R.id.tv_active_habits_count)
        val habitoViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[HabitoRoomViewModel::class.java]
        lifecycleScope.launch {
            habitoViewModel.getCompletadosUltimoMes().collectLatest { lista ->
                tvCompletedHabits.text = lista.size.toString()
            }
        }
        lifecycleScope.launch {
            habitoViewModel.getActivos().collectLatest { lista ->
                tvActiveHabits.text = lista.size.toString()
            }
        }

        return view
    }
}
