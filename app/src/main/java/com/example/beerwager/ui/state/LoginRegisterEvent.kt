package com.example.beerwager.ui.state

sealed class LoginRegisterEvent {
    data class UsernameChangedEvent(val username: String) : LoginRegisterEvent()
    data class EmailChangedEvent(val email: String) : LoginRegisterEvent()
    data class PasswordChangedEvent(val password: String) : LoginRegisterEvent()
    data class ConfirmPasswordChangedEvent(val confirmPassword: String) : LoginRegisterEvent()
    object LoginRegisterScreenToggleEvent : LoginRegisterEvent()
    object LoginEvent : LoginRegisterEvent()
    object RegisterEvent : LoginRegisterEvent()
}