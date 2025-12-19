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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.R
import com.example.habitapp.data.entity.Habito
import com.example.habitapp.viewmodel.HabitoRoomViewModel
import com.google.android.material.textfield.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HabitsFragment : Fragment() {
    private val viewModel: HabitoRoomViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }

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
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(
                v.paddingLeft,
                statusBarHeight,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        headerTitle.text = getString(R.string.title_habits)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_habits)

        val adapter = HabitsAdapter { habit ->
            val detailIntent = Intent(requireContext(), HabitDetailActivity::class.java)
            detailIntent.putExtra("habit_id", habit.idHabito)
            startActivity(detailIntent)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter

        // FAB para agregar hábito
        val fab = view.findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab_add_habit)
        fab.setOnClickListener {
            startActivity(Intent(requireContext(), AddHabitActivity::class.java))
        }

        // Recoger los hábitos desde Room
        val etSearch = view.findViewById<TextInputEditText>(R.id.et_search_habit)
        var listaActual: List<Habito> = emptyList()
        lifecycleScope.launch {
            viewModel.habitos.collectLatest { lista ->
                listaActual = lista
                adapter.submitList(lista)
                headerSubtitle.text = getString(R.string.habits_active_count, lista.size)
            }
        }
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim()?.lowercase() ?: ""
                if (query.isEmpty()) {
                    adapter.submitList(listaActual)
                } else {
                    val filtrada = listaActual.filter { habito ->
                        habito.titulo.lowercase().contains(query) || (habito.descripcion?.lowercase()?.contains(query) ?: false)
                    }
                    adapter.submitList(filtrada)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}

private class HabitsAdapter(
    val onClick: (Habito) -> Unit
) : ListAdapter<Habito, HabitViewHolder>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Habito>() {
            override fun areItemsTheSame(oldItem: Habito, newItem: Habito): Boolean = oldItem.idHabito == newItem.idHabito
            override fun areContentsTheSame(oldItem: Habito, newItem: Habito): Boolean = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class HabitViewHolder(
    itemView: View,
    val onClick: (Habito) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.text_habit_name)
    private val desc: TextView = itemView.findViewById(R.id.text_habit_desc)
    private val status: TextView = itemView.findViewById(R.id.text_habit_status)
    private val progress: android.widget.ProgressBar = itemView.findViewById(R.id.progress_habit)
    private val progressText: TextView = itemView.findViewById(R.id.text_habit_progress)

    fun bind(habit: Habito) {
        name.text = habit.titulo
        desc.text = habit.descripcion
        status.text = if (habit.completado) itemView.context.getString(R.string.habit_status_completed) else itemView.context.getString(R.string.habit_status_pending)
        progress.progress = if (habit.completado) 100 else 0
        progressText.text = if (habit.completado) "100%" else "0%"

        itemView.setOnClickListener { onClick(habit) }
    }
}
