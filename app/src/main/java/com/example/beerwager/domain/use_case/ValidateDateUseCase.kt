package com.example.beerwager.domain.use_case

import com.example.beerwager.R
import com.example.beerwager.domain.models.ValidationResult
import java.time.LocalDate
import javax.inject.Inject

class ValidateDateUseCase @Inject constructor() {

    operator fun invoke(date: LocalDate?): ValidationResult {
        date?.let { return ValidationResult(true) }
        return ValidationResult(false, R.string.text_empty_date)
    }
}