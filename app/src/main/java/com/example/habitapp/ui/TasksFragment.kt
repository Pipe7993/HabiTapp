package com.example.habitapp.ui

import android.content.Intent
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
import com.example.habitapp.viewmodel.TasksViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasksFragment : Fragment() {
    private lateinit var adapter: TasksAdapter
    private lateinit var viewModel: TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        val header = view.findViewById<View>(R.id.header_tasks)
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

        headerTitle.text = "Tareas de Hoy"
        val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("es", "ES"))
        headerSubtitle.text = dateFormat.format(Date())

        viewModel = ViewModelProvider(requireActivity())[TasksViewModel::class.java]

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_tasks)
        adapter = TasksAdapter { item ->
            val intent = Intent(requireContext(), TaskDetailActivity::class.java)
            intent.putExtra("TASK_ID", item.id)
            startActivity(intent)
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_task)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), AddTaskActivity::class.java)
            startActivity(intent)
        }

        viewModel.tasks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        return view
    }
}
