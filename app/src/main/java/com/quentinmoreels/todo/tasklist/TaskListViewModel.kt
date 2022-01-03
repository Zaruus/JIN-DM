package com.quentinmoreels.todo.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    public val taskList: StateFlow<List<Task>> = _taskList

    fun loadTasks() {
        viewModelScope.launch {
            val tasks = repository.loadTasks()
            _taskList.value = tasks!!
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.removeTask(task)
            loadTasks()
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.createTask(task)
            loadTasks()
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = repository.updateTask(task)
            if (updatedTask != null) {
                val oldTask = taskList.value.firstOrNull { it.id == updatedTask.id }
                if (oldTask != null) _taskList.value = taskList.value - oldTask + updatedTask
            }
            loadTasks()
        }
    }
}