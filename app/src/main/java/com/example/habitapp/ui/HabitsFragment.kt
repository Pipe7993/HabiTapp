package com.example.habitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habitapp.R
import com.example.habitapp.viewmodel.HabitsViewModel

class HabitsFragment : Fragment() {
    private lateinit var adapter: HabitsAdapter
    private lateinit var viewModel: HabitsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_habits, container, false)

        viewModel = ViewModelProvider(requireActivity())[HabitsViewModel::class.java]

        val rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv_habits)
        adapter = HabitsAdapter { item ->
            
        }
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        return view
    }
}
