package com.example.beerwager.domain.use_case

import com.example.beerwager.R
import com.example.beerwager.domain.models.ValidationResult
import javax.inject.Inject

class ValidateWagerersUseCase @Inject constructor() {

    operator fun invoke(wagerers: List<String>): ValidationResult {
        if (wagerers.isEmpty()) return ValidationResult(false, R.string.text_no_wagerers)
        return ValidationResult(true)
    }

}