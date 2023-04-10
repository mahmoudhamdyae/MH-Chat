package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.passwordErrorMessage

class ValidatePassword {

    operator fun invoke(password: String, isLogIn: Boolean = false): ValidationResult {
        val passwordErrorMessage = password.passwordErrorMessage()
        return if (passwordErrorMessage == null) {
            ValidationResult(
                successful = true,
            )
        } else if (passwordErrorMessage == R.string.empty_password_error) {
            ValidationResult(
                successful = false,
                errorMessage = passwordErrorMessage
            )
        } else if (!isLogIn) {
            ValidationResult(
                successful = false,
                errorMessage = passwordErrorMessage
            )
        } else {
            ValidationResult(
                successful = true
            )
        }
    }
}