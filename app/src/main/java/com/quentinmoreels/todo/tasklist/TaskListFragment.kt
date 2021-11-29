package com.quentinmoreels.todo.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.quentinmoreels.todo.databinding.FragmentTaskListBinding
import com.quentinmoreels.todo.form.FormActivity

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    val myAdapter = TaskListAdapter()
    private var taskList = mutableListOf(
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
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = myAdapter
        myAdapter.submitList(taskList.toList())

        myAdapter.onClickDelete = { task ->
            taskList.remove(task)
            myAdapter.submitList(taskList.toList())
        }

        myAdapter.onClickModify = { task ->
            //taskList.remove(task)
            val intent = Intent(activity, FormActivity::class.java)
            intent.putExtra("task", task)
            formLauncher.launch(intent)
        }

        val actionButton = binding.floatingActionButton
        actionButton.setOnClickListener { view ->
            // Instanciation d'un objet task avec des données préremplies:
            val intent = Intent(activity, FormActivity::class.java)
            formLauncher.launch(intent)
        }
    }

    val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        val recyclerView = binding.recyclerView
        val task = result.data?.getSerializableExtra("task") as? Task
        recyclerView.adapter = myAdapter

        if (task != null) {
            val oldTask = taskList.firstOrNull { it.id == task.id }
            if (oldTask != null) taskList = (taskList - oldTask) as MutableList<Task>

            taskList.add(task)
            myAdapter.submitList(taskList.toList())
        }
    }
}