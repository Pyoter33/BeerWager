package com.example.beerwager.domain.models

import androidx.annotation.StringRes

data class ValidationResult(val isSuccessful: Boolean, @StringRes val message: Int? = null)