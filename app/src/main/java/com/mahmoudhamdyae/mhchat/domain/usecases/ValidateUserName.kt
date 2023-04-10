package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.isValidUserName

class ValidateUserName {

    operator fun invoke(userName: String): ValidationResult {
        return if (userName.isValidUserName()) {
            ValidationResult(
                successful = true,
            )
        } else {
            ValidationResult(
                successful = false,
                errorMessage = R.string.user_name_error
            )
        }
    }
}