package com.example.habitapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.R
import com.example.habitapp.model.Habito

class HabitsFragment : Fragment() {
    private val viewModel: HabitsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_habits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.recycler_habits)
        val btnAdd = view.findViewById<Button>(R.id.btn_add_habit)

        val adapter = HabitsAdapter { habit ->
            val detailIntent = Intent(requireContext(), HabitDetailActivity::class.java)
            detailIntent.putExtra("habit_id", habit.id)
            startActivity(detailIntent)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        viewModel.habits.observe(viewLifecycleOwner, Observer { lista ->
            adapter.submitList(lista)
        })

        btnAdd.setOnClickListener {
            startActivity(Intent(requireContext(), AddHabitActivity::class.java))
        }
    }
}

private class HabitsAdapter(
    val onClick: (Habito) -> Unit
) : RecyclerView.Adapter<HabitViewHolder>() {
    private var items: List<Habito> = emptyList()

    fun submitList(newItems: List<Habito>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view, onClick)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

private class HabitViewHolder(
    itemView: View,
    val onClick: (Habito) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    fun bind(habit: Habito) {
        val name = itemView.findViewById<android.widget.TextView>(R.id.text_habit_name)
        val desc = itemView.findViewById<android.widget.TextView>(R.id.text_habit_desc)
        name.text = habit.Nombre
        desc.text = habit.Descripcion
        itemView.setOnClickListener { onClick(habit) }
    }
}
