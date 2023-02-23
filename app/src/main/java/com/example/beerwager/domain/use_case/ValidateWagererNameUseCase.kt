package com.example.beerwager.domain.use_case

import com.example.beerwager.R
import com.example.beerwager.domain.models.ValidationResult
import javax.inject.Inject

class ValidateWagererNameUseCase @Inject constructor() {

    operator fun invoke(name: String): ValidationResult {
        if (name.isEmpty()) return ValidationResult(false, R.string.text_empty_wagerer)
        return ValidationResult(true)
    }

}