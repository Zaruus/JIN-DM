package com.quentinmoreels.todo.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String = "defaultEmail",
    @SerialName("firstname")
    val firstName: String = "defaultFirstName",
    @SerialName("lastname")
    val lastName: String = "defaultLastName",
    @SerialName("avatar")
    val avatar: String = "defaultAvatar"
)