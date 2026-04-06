package com.example.beerwager.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.domain.models.LoginRegisterErrorType
import com.example.beerwager.domain.use_case.LoginUseCase
import com.example.beerwager.domain.use_case.RegisterUseCase
import com.example.beerwager.domain.use_case.ValidateLoginRegisterFieldsUseCase
import com.example.beerwager.ui.state.LoginRegisterEvent
import com.example.beerwager.ui.state.LoginRegisterState
import com.example.beerwager.utils.HttpRequestException
import com.example.beerwager.utils.TokenManager
import com.example.beerwager.utils.toLoginDto
import com.example.beerwager.utils.toRegisterDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val tokenManager: TokenManager,
    private val validateFields: ValidateLoginRegisterFieldsUseCase
) : ViewModel() {

    private val _loginRegisterState = MutableStateFlow(LoginRegisterState())
    val loginRegisterState = _loginRegisterState.asStateFlow()

    private val errorChannel = Channel<LoginRegisterErrorType>()
    val errorFlow = errorChannel.receiveAsFlow()

    private val _navigationChannel = Channel<Unit>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    fun onEvent(event: LoginRegisterEvent) {
        when(event) {
            is LoginRegisterEvent.ConfirmPasswordChangedEvent -> {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordError = null
                    )
                }
            }
            is LoginRegisterEvent.EmailChangedEvent -> {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(
                        email = event.email,
                        emailError = null
                    )
                }
            }
            LoginRegisterEvent.LoginEvent -> {
                resetErrors()
                logInAndSaveToken()
            }
            LoginRegisterEvent.LoginRegisterScreenToggleEvent -> {
                _loginRegisterState.update {
                    LoginRegisterState().copy(isLogin = !loginRegisterState.value.isLogin)
                }
            }
            is LoginRegisterEvent.PasswordChangedEvent -> {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(
                        password = event.password,
                        passwordError = null
                    )
                }
            }
            LoginRegisterEvent.RegisterEvent -> {
                resetErrors()
                registerUserAndSaveToken()
            }
            is LoginRegisterEvent.UsernameChangedEvent -> {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(
                        username = event.username,
                        usernameError = null
                    )
                }
            }
        }
    }

    private fun logInAndSaveToken() {
        viewModelScope.launch {
            val usernameError = validateFields.validateUsername(loginRegisterState.value.username)
            val passwordError = validateFields.validatePassword(loginRegisterState.value.password)

            if (usernameError != null) {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(usernameError = usernameError)
                }
                return@launch
            }
            if (passwordError != null) {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(passwordError = passwordError)
                }
                return@launch
            }
            loginUseCase(loginRegisterState.value.toLoginDto()).fold(
                onSuccess = { token ->
                    tokenManager.saveToken(token)
                    _navigationChannel.send(Unit)
                },
                onFailure = { exception ->
                    when (exception) {
                        is HttpRequestException -> {
                            when (exception.errorCode) {
                                401 -> {
                                    _loginRegisterState.update {
                                        loginRegisterState.value.copy(
                                            passwordError = LoginRegisterErrorType.PASSWORD_INCORRECT
                                        )
                                    }
                                }
                                404 -> {
                                    _loginRegisterState.update {
                                        loginRegisterState.value.copy(
                                            usernameError = LoginRegisterErrorType.USERNAME_NOT_EXISTS
                                        )
                                    }
                                }
                                else -> {
                                    errorChannel.send(LoginRegisterErrorType.UNEXPECTED_ERROR)
                                }
                            }
                        }
                        else -> {
                            errorChannel.send(LoginRegisterErrorType.UNEXPECTED_ERROR)
                        }
                    }
                }
            )
        }
    }

    private fun registerUserAndSaveToken() {
        viewModelScope.launch {
            val usernameError = validateFields.validateUsername(loginRegisterState.value.username)
            val emailError = validateFields.validateEmail(loginRegisterState.value.email)
            val passwordError = validateFields.validatePassword(loginRegisterState.value.password)
            val confirmPasswordError = validateFields.validateConfirmPassword(
                loginRegisterState.value.password,
                loginRegisterState.value.confirmPassword
            )

            if (usernameError != null) {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(usernameError = usernameError)
                }
                return@launch
            }
            if (emailError != null) {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(emailError = emailError)
                }
                return@launch
            }
            if (passwordError != null) {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(passwordError = passwordError)
                }
                return@launch
            }
            if (confirmPasswordError != null) {
                _loginRegisterState.update {
                    loginRegisterState.value.copy(confirmPasswordError = confirmPasswordError)
                }
                return@launch
            }

            registerUseCase(loginRegisterState.value.toRegisterDto()).fold(
                onSuccess = { token ->
                    tokenManager.saveToken(token)
                    _navigationChannel.send(Unit)
                },
                onFailure = { exception ->
                    when (exception) {
                        is HttpRequestException -> {
                            when (exception.errorCode) {
                                409 -> {
                                    _loginRegisterState.update {
                                        loginRegisterState.value.copy(
                                            emailError = LoginRegisterErrorType.USERNAME_TAKEN
                                        )
                                    }
                                }

                                else -> {
                                    errorChannel.send(LoginRegisterErrorType.UNEXPECTED_ERROR)
                                }
                            }
                        }

                        else -> {
                            errorChannel.send(LoginRegisterErrorType.UNEXPECTED_ERROR)
                        }
                    }
                }
            )
        }
    }

    private fun resetErrors() {
        _loginRegisterState.update {
            loginRegisterState.value.copy(
                usernameError = null,
                emailError = null,
                passwordError = null,
                confirmPasswordError = null
            )
        }
    }
}