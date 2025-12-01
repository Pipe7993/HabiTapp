package com.example.habitapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.R
import com.example.habitapp.viewmodel.Task

class TasksAdapter(private val onClick: (Task) -> Unit) :
    ListAdapter<Task, TasksAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.tv_task_title)
        private val subtitle: TextView = view.findViewById(R.id.tv_task_subtitle)
        private val time: TextView = view.findViewById(R.id.tv_task_time)
        private val priority: TextView = view.findViewById(R.id.tv_task_priority)

        fun bind(item: Task) {
            title.text = item.title
            subtitle.text = item.subtitle
            time.text = item.time
            priority.text = item.priority
            when (item.priority.lowercase()) {
                "alta" -> priority.setBackgroundResource(R.drawable.bg_tag_red)
                "media" -> priority.setBackgroundResource(R.drawable.bg_tag_orange)
                "baja" -> priority.setBackgroundResource(R.drawable.bg_tag_blue)
                else -> priority.setBackgroundResource(R.drawable.bg_tag_blue)
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
        }
    }
}
