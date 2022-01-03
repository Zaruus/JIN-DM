package com.quentinmoreels.todo.tasklist

import kotlinx.serialization.SerialName
import java.io.Serializable

/*data class Task(val id: String, val title: String, val description: String = "default description") : Serializable*/
@kotlinx.serialization.Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String = "default description"
): Serializable
