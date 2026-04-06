package com.example.beerwager.domain.models

import androidx.annotation.StringRes
import com.example.beerwager.R

enum class LoginRegisterErrorType(@StringRes val messageId: Int) {
    EMAIL_FORMAT_INVALID(R.string.login_error_email_format),
    USERNAME_TAKEN(R.string.login_error_username),
    USERNAME_NOT_EXISTS(R.string.login_error_username_not_exists),
    PASSWORD_INCORRECT(R.string.login_error_password_incorrect),
    PASSWORD_DIFFERS(R.string.login_error_passwords_differs),
    FIELD_EMPTY(R.string.login_error_field_empty),
    UNEXPECTED_ERROR(R.string.login_error_unexpected);
}
