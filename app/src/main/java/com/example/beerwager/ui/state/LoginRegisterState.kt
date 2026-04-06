package com.example.beerwager.ui.state

import com.example.beerwager.domain.models.LoginRegisterErrorType
import com.example.beerwager.utils.empty

data class LoginRegisterState(
    val username: String = String.empty(),
    val email: String = String.empty(),
    val password: String = String.empty(),
    val confirmPassword: String = String.empty(),
    val isLogin: Boolean = true,
    val isLoading: Boolean = false,
    val emailError: LoginRegisterErrorType? = null,
    val passwordError: LoginRegisterErrorType? = null,
    val confirmPasswordError: LoginRegisterErrorType? = null,
    val usernameError : LoginRegisterErrorType? = null
)