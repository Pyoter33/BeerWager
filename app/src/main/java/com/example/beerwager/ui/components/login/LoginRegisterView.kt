package com.example.beerwager.ui.components.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.ui.state.LoginRegisterEvent
import com.example.beerwager.ui.state.LoginRegisterState
import com.example.beerwager.utils.Dimen

@Composable
private fun LoginView(loginState: LoginRegisterState, onEvent: (LoginRegisterEvent) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = Dimen.SPACING_M)) {
        LoginTitle()
        Spacer(Modifier.size(Dimen.SPACING_XL))
        LoginTextField(
            stringResource(R.string.login_username),
            isPassword = false,
            value = loginState.username,
            error = loginState.usernameError?.let { stringResource(it.messageId) },
            hint = stringResource(R.string.login_username_hint),
            onValueChanged = {
                onEvent(LoginRegisterEvent.UsernameChangedEvent(it))
            }
        )
        Spacer(Modifier.size(Dimen.SPACING_XS))
        LoginTextField(
            stringResource(R.string.login_password),
            isPassword = true,
            error = loginState.passwordError?.let { stringResource(it.messageId) },
            value = loginState.password,
            hint = stringResource(R.string.login_password_hint),
            onValueChanged = {
                onEvent(LoginRegisterEvent.PasswordChangedEvent(it))
            }
        )
        Spacer(Modifier.size(Dimen.SPACING_XS))
        TextButton(
            onClick = {
                onEvent(LoginRegisterEvent.LoginRegisterScreenToggleEvent)
            }
        ) {
            Text(
                text = stringResource(R.string.login_go_to_register),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.size(Dimen.SPACING_M))
        Button(
            onClick = {
                onEvent(LoginRegisterEvent.LoginEvent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.login_log_in),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(Dimen.SPACING_XS)
            )
        }
    }
}

@Composable
private fun RegisterView(registerState: LoginRegisterState, onEvent: (LoginRegisterEvent) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = Dimen.SPACING_M)) {
        LoginTitle()
        Spacer(Modifier.size(Dimen.SPACING_XL))
        LoginTextField(
            stringResource(R.string.login_username),
            isPassword = false,
            value = registerState.username,
            error = registerState.usernameError?.let { stringResource(it.messageId) },
            hint = stringResource(R.string.login_username_hint),
            onValueChanged = {
                onEvent(LoginRegisterEvent.UsernameChangedEvent(it))
            }
        )
        Spacer(Modifier.size(Dimen.SPACING_XS))
        LoginTextField(
            stringResource(R.string.login_email),
            isPassword = false,
            value = registerState.email,
            error = registerState.emailError?.let { stringResource(it.messageId) },
            hint = stringResource(R.string.login_email_hint),
            onValueChanged = {
                onEvent(LoginRegisterEvent.EmailChangedEvent(it))
            }
        )
        Spacer(Modifier.size(Dimen.SPACING_XS))
        LoginTextField(
            stringResource(R.string.login_password),
            isPassword = true,
            value = registerState.password,
            error = registerState.passwordError?.let { stringResource(it.messageId) },
            hint = stringResource(R.string.login_password_hint),
            onValueChanged = {
                onEvent(LoginRegisterEvent.PasswordChangedEvent(it))
            }
        )
        Spacer(Modifier.size(Dimen.SPACING_XS))
        LoginTextField(
            stringResource(R.string.login_confirm_password),
            isPassword = true,
            value = registerState.confirmPassword,
            error = registerState.confirmPasswordError?.let { stringResource(it.messageId) },
            hint = stringResource(R.string.login_password_hint),
            onValueChanged = {
                onEvent(LoginRegisterEvent.ConfirmPasswordChangedEvent(it))
            }
        )
        Spacer(Modifier.size(Dimen.SPACING_XS))
        TextButton(
            onClick = {
                onEvent(LoginRegisterEvent.LoginRegisterScreenToggleEvent)
            }
        ) {
            Text(
                text = stringResource(R.string.login_go_to_login),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Spacer(Modifier.size(Dimen.SPACING_M))
        Button(
            onClick = {
                onEvent(LoginRegisterEvent.RegisterEvent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.login_register),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(Dimen.SPACING_XS)
            )
        }
    }
}

@Composable
fun LoginRegisterView(
    loginRegisterState: LoginRegisterState,
    onEvent: (LoginRegisterEvent) -> Unit
) {
    if (loginRegisterState.isLogin) {
        LoginView(loginRegisterState, onEvent)
    } else {
        RegisterView(loginRegisterState, onEvent)
    }
}
