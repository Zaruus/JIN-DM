package com.quentinmoreels.todo.tasklist

import com.quentinmoreels.todo.network.Api

class TasksRepository {
    // Le web service requête le serveur
    private val tasksWebService = Api.tasksWebService

    suspend fun createTask(task: Task) {
        tasksWebService.create(task)
    }

    suspend fun removeTask(task: Task) {
        tasksWebService.delete(task.id)
    }

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateTask(task: Task): Task? {
        val response = tasksWebService.update(task, task.id)
        return if(response.isSuccessful) response.body() else null
    }
}