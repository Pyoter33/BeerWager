package com.example.beerwager.domain.use_case

import com.example.beerwager.R
import com.example.beerwager.domain.models.ValidationResult
import javax.inject.Inject

class ValidateDescriptionUseCase @Inject constructor() {

    operator fun invoke(description: String): ValidationResult {
        if (description.isEmpty()) return ValidationResult(false, R.string.text_empty_description)
        return ValidationResult(true)
    }

}