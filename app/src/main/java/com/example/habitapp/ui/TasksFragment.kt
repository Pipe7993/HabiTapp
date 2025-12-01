package com.example.habitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.R
import com.example.habitapp.viewmodel.TasksViewModel

class TasksFragment : Fragment() {
    private lateinit var adapter: TasksAdapter
    private lateinit var viewModel: TasksViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        viewModel = ViewModelProvider(requireActivity())[TasksViewModel::class.java]

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_tasks)
        adapter = TasksAdapter { item ->
            
            viewModel.toggleDone(item.id)
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewModel.tasks.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        return view
    }
}
