package com.example.habitapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.R
import com.example.habitapp.viewmodel.TareaRoomViewModel
import com.example.habitapp.data.entity.Tarea
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class TasksFragment : Fragment() {
    private lateinit var adapter: TasksAdapter
    private lateinit var tareaViewModel: TareaRoomViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        val header = view.findViewById<View>(R.id.header_tasks)
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

        headerTitle.text = "Tareas"
        val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
        headerSubtitle.text = dateFormat.format(Date())

        tareaViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[TareaRoomViewModel::class.java]

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recycler_tasks)
        adapter = TasksAdapter { tarea ->
            val intent = Intent(requireContext(), TaskDetailActivity::class.java)
            intent.putExtra("TASK_ID", tarea.idTarea)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_task)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            startActivity(intent)
        }

        val etSearch = view.findViewById<TextInputEditText>(R.id.et_search_task)
        var listaActual: List<Tarea> = emptyList()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                tareaViewModel.tareas.collectLatest { lista ->
                    listaActual = lista.sortedWith(compareBy<Tarea> {
                        it.estado == com.example.habitapp.data.entity.EstadoTarea.COMPLETADA
                    }.thenBy {
                        it.fechaLimite ?: Long.MAX_VALUE
                    })
                    adapter.submitList(listaActual)
                }
            }
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim()?.lowercase() ?: ""
                if (query.isEmpty()) {
                    adapter.submitList(listaActual)
                } else {
                    val filtrada = listaActual.filter { tarea ->
                        tarea.titulo.lowercase().contains(query) || tarea.descripcion.lowercase().contains(query)
                    }
                    adapter.submitList(filtrada)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }
}
