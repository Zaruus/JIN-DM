package com.quentinmoreels.todo.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginForm(
    @SerialName("email")
    val email: String = "defaultEmail",
    @SerialName("password")
    val password: String = "defaultPassword",
)
