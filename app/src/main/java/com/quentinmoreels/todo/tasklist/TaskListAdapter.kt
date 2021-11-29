package com.quentinmoreels.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.quentinmoreels.todo.R
import java.util.*

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        // are they the same "entity" ? (usually same id)
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.description == newItem.description
    // do they have the same data ? (content)
}

class TaskListAdapter() : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {
    var onClickDelete: (Task) -> Unit = {}
    var onClickModify: (Task) -> Unit = {}

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            val textViewTitle = itemView.findViewById<TextView>(R.id.task_title)
            val textViewDescription = itemView.findViewById<TextView>(R.id.task_description)
            textViewTitle.text = task.title
            textViewDescription.text = task.description

            val deleteButton = itemView.findViewById<ImageButton>(R.id.butDelete)
            deleteButton.setOnClickListener { view ->
                onClickDelete(task)
            }

            val modifyButton = itemView.findViewById<ImageButton>(R.id.butModifyTask)
            modifyButton.setOnClickListener { view ->
                onClickModify(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}