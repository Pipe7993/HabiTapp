package com.example.habitapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habitapp.R
import com.example.habitapp.data.entity.Prioridad
import com.example.habitapp.data.entity.Tarea

class TasksAdapter(private val onClick: (Tarea) -> Unit) :
    ListAdapter<Tarea, TasksAdapter.VH>(DIFF) {

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

        fun bind(item: Tarea) {
            title.text = item.titulo
            subtitle.text = item.descripcion
            time.text = item.fechaLimite?.let { java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(it)) } ?: "Sin fecha"
            priority.text = when (item.prioridad) {
                Prioridad.ALTA -> "Alta"
                Prioridad.MEDIA -> "Media"
                Prioridad.BAJA -> "Baja"
            }
            when (item.prioridad) {
                Prioridad.ALTA -> priority.setBackgroundResource(R.drawable.bg_tag_red)
                Prioridad.MEDIA -> priority.setBackgroundResource(R.drawable.bg_tag_orange)
                Prioridad.BAJA -> priority.setBackgroundResource(R.drawable.bg_tag_blue)
            }
            // Visual para completadas
            if (item.estado == com.example.habitapp.data.entity.EstadoTarea.COMPLETADA) {
                title.paintFlags = title.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                subtitle.paintFlags = subtitle.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
                itemView.alpha = 0.5f
            } else {
                title.paintFlags = title.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                subtitle.paintFlags = subtitle.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
                itemView.alpha = 1f
            }
        }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Tarea>() {
            override fun areItemsTheSame(oldItem: Tarea, newItem: Tarea) = oldItem.idTarea == newItem.idTarea
            override fun areContentsTheSame(oldItem: Tarea, newItem: Tarea) = oldItem == newItem
        }
    }
}
