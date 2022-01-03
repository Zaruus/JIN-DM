package com.quentinmoreels.todo.userinfo

import com.quentinmoreels.todo.authentication.LoginForm
import com.quentinmoreels.todo.authentication.LoginResponse
import com.quentinmoreels.todo.authentication.SignUpForm
import com.quentinmoreels.todo.authentication.SignUpResponse
import com.quentinmoreels.todo.network.Api
import com.quentinmoreels.todo.network.UserInfo
import okhttp3.MultipartBody
import retrofit2.http.Body

class UserInfoRepository {
    private val userWebService = Api.userWebService

    suspend fun loadUserInfo(): UserInfo? {
        val response = userWebService.getInfo()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateUserInfo(userInfo: UserInfo): UserInfo? {
        val response = userWebService.update(userInfo)
        return if(response.isSuccessful) response.body() else null
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part): UserInfo? {
        val response = userWebService.updateAvatar(avatar)
        return if(response.isSuccessful) response.body() else null
    }

    suspend fun loginUser(userLoginForm: LoginForm): LoginResponse? {
        val response = userWebService.login(userLoginForm)
        return if(response.isSuccessful) response.body() else null
    }

    suspend fun signUpUser(userSignUpForm: SignUpForm): SignUpResponse? {
        val response = userWebService.signUp(userSignUpForm)
        return if(response.isSuccessful) response.body() else null
    }
}