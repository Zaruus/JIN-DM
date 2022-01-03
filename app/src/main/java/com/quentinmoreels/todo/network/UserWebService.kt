package com.quentinmoreels.todo.network

import com.quentinmoreels.todo.authentication.LoginForm
import com.quentinmoreels.todo.authentication.LoginResponse
import com.quentinmoreels.todo.authentication.SignUpForm
import com.quentinmoreels.todo.authentication.SignUpResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>

    @POST("users/sign_up")
    suspend fun signUp(@Body user: SignUpForm): Response<SignUpResponse>
}