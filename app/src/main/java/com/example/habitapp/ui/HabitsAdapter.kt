package com.example.habitapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.R
import com.example.habitapp.viewmodel.Habit

class HabitsAdapter(private val onClick: (Habit) -> Unit) :
    ListAdapter<Habit, HabitsAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_habit_title)
        private val subtitle: TextView = view.findViewById(R.id.tv_habit_sub)
        private val progress: ProgressBar = view.findViewById(R.id.pb_habit)

        fun bind(item: Habit) {
            title.text = item.title
            subtitle.text = item.description
            progress.progress = item.progress
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Habit>() {
            override fun areItemsTheSame(oldItem: Habit, newItem: Habit) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Habit, newItem: Habit) = oldItem == newItem
        }
    }
}
