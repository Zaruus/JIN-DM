package com.quentinmoreels.todo.userinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quentinmoreels.todo.authentication.LoginForm
import com.quentinmoreels.todo.authentication.LoginResponse
import com.quentinmoreels.todo.authentication.SignUpForm
import com.quentinmoreels.todo.authentication.SignUpResponse
import com.quentinmoreels.todo.network.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel: ViewModel() {
    private val repository = UserInfoRepository()
    private val _userInfo = MutableStateFlow<UserInfo>(UserInfo())
    private val _loginResponse = MutableStateFlow<LoginResponse>(LoginResponse())
    private val _signUpResponse = MutableStateFlow<SignUpResponse>(SignUpResponse())
    public val userInfo: StateFlow<UserInfo> = _userInfo
    public val loginResponse: StateFlow<LoginResponse> = _loginResponse
    public val signUpResponse: StateFlow<SignUpResponse> = _signUpResponse

    fun loadUserInfo() {
        viewModelScope.launch {
            val info = repository.loadUserInfo()
            _userInfo.value = info!!
        }
    }

    fun editUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            val updatedUserInfo = repository.updateUserInfo(userInfo)
            if (updatedUserInfo != null) {
                _userInfo.value = updatedUserInfo
            }
            loadUserInfo()
        }
    }

    fun editAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val updatedAvatarUserInfo = repository.updateAvatar(avatar)
            if (updatedAvatarUserInfo != null) {
                _userInfo.value = updatedAvatarUserInfo
            }
            loadUserInfo()
        }
    }

    fun loginUserInfo(userLoginForm: LoginForm) {
        viewModelScope.launch {
            val newLoginResponse = repository.loginUser(userLoginForm)
            if(newLoginResponse != null) {
                _loginResponse.value = newLoginResponse
            }
        }
    }

    fun signUpUserInfo(userSignUpForm: SignUpForm) {
        viewModelScope.launch {
            val newSignUpResponse = repository.signUpUser(userSignUpForm)
            if(newSignUpResponse != null) {
                _signUpResponse.value = newSignUpResponse
            }
        }
    }

}