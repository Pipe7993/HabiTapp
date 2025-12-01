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
        headerSubtitle.text = "4 hábitos activos"

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_habits)

        val adapter = HabitsAdapter { habit ->
            val detailIntent = Intent(requireContext(), HabitDetailActivity::class.java)
            detailIntent.putExtra("habit_id", habit.id)
            startActivity(detailIntent)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // FAB para agregar hábito
        val fab = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_add_habit)
        fab.setOnClickListener {
            startActivity(Intent(requireContext(), AddHabitActivity::class.java))
        }

        viewModel.habits.observe(viewLifecycleOwner, Observer { lista ->
            adapter.submitList(lista)
            // Actualizar subtítulo del header con el número de hábitos
            headerSubtitle.text = "${lista.size} hábitos activos"
        })
    }
}

// ...existing code...

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
        val status = itemView.findViewById<android.widget.TextView>(R.id.text_habit_status)
        val frequency = itemView.findViewById<android.widget.TextView>(R.id.text_habit_frequency)
        val progress = itemView.findViewById<android.widget.ProgressBar>(R.id.progress_habit)
        val progressText = itemView.findViewById<android.widget.TextView>(R.id.text_habit_progress)

        name.text = habit.Nombre
        desc.text = habit.Descripcion
        status.text = if (habit.Activo) "Activo" else "Inactivo"
        frequency.text = habit.Frecuencia
        progress.progress = habit.Progreso
        progressText.text = "${habit.Progreso}%"

        itemView.setOnClickListener { onClick(habit) }
    }
}
