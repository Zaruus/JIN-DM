package com.quentinmoreels.todo.tasklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.quentinmoreels.todo.R
import com.quentinmoreels.todo.databinding.FragmentTaskListBinding
import java.util.*

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val taskList = mutableListOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.recyclerView
        val myAdapter = TaskListAdapter(taskList)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = myAdapter
        myAdapter.submitList(taskList.toList())

        val actionButton = binding.floatingActionButton
        actionButton.setOnClickListener { view ->
            // Instanciation d'un objet task avec des données préremplies:
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}")
            taskList.add(newTask)
            myAdapter.submitList(taskList.toList())
        }
    }
}