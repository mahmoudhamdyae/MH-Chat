package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.isValidEmail

class ValidateEmail() {

    operator fun invoke(email: String): ValidationResult {
        return if (email.isValidEmail()) {
            ValidationResult(
                successful = true,
            )
        } else {
            ValidationResult(
                successful = false,
                errorMessage = R.string.email_error
            )
        }
    }
}