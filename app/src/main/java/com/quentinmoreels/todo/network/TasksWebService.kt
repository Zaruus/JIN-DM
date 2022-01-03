package com.quentinmoreels.todo.network

import com.quentinmoreels.todo.tasklist.Task
import retrofit2.Response
import retrofit2.http.*

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("tasks")
    suspend fun create(@Body task: Task): Response<Task>

    @PATCH("tasks/{id}")
    suspend fun update(@Body task: Task, @Path("id") id: String? = task.id): Response<Task>

// Inspirez vous d'au dessus et de la doc de l'API pour compléter:
    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>
}