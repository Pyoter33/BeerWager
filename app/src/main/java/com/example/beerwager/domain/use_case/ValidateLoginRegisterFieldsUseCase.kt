package com.example.beerwager.domain.use_case

import com.example.beerwager.domain.models.LoginRegisterErrorType
import javax.inject.Inject

class ValidateLoginRegisterFieldsUseCase @Inject constructor() {

    companion object {
        private const val EMAIL_REGEX = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    }

    fun validateUsername(username: String): LoginRegisterErrorType? {
        if (username.isBlank()) return LoginRegisterErrorType.FIELD_EMPTY
        return null
    }

    fun validateEmail(email: String): LoginRegisterErrorType? {
        if (email.isBlank()) return LoginRegisterErrorType.FIELD_EMPTY
        if (!email.matches(EMAIL_REGEX.toRegex())) return LoginRegisterErrorType.EMAIL_FORMAT_INVALID
        return null
    }

    fun validatePassword(password: String): LoginRegisterErrorType? {
        if (password.isBlank()) return LoginRegisterErrorType.FIELD_EMPTY
        return null
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): LoginRegisterErrorType? {
        if (confirmPassword.isBlank()) return LoginRegisterErrorType.FIELD_EMPTY
        if (password != confirmPassword) return LoginRegisterErrorType.PASSWORD_DIFFERS
        return null
    }
}

