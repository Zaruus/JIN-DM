package com.quentinmoreels.todo.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpForm(
    @SerialName("firstName")
    val firstName: String = "defaultFirstName",
    @SerialName("lastName")
    val lastName: String = "defaultLastName",
    @SerialName("email")
    val email: String = "defaultEmail",
    @SerialName("password")
    val password: String = "defaultPassword",
    @SerialName("passwordConfirmation")
    val passwordConfirmation: String = "defaultPasswordConfirmation",
)
