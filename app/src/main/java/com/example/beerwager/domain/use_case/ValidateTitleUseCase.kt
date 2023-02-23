package com.example.beerwager.domain.use_case

import com.example.beerwager.R
import com.example.beerwager.domain.models.ValidationResult
import javax.inject.Inject

class ValidateTitleUseCase @Inject constructor() {

    operator fun invoke(title: String): ValidationResult {
        if (title.isEmpty()) return ValidationResult(false, R.string.text_empty_title)
        return ValidationResult(true)
    }

}