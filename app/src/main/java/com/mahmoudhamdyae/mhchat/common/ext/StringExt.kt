package com.mahmoudhamdyae.mhchat.common.ext

import android.util.Patterns
import com.mahmoudhamdyae.mhchat.R
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val PASS_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

fun String.isValidUserName(): Boolean {
    return this.isNotBlank()
}

fun String.isValidEmail(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

//fun String.isValidPassword(): Boolean {
//    return this.isNotBlank() &&
//            this.length >= MIN_PASS_LENGTH &&
//            Pattern.compile(PASS_PATTERN).matcher(this).matches()
//}
fun String.passwordErrorMessage(): Int? {
    return if (this.isBlank()) {
        R.string.empty_password_error
    } else if (this.length >= MIN_PASS_LENGTH) {
        R.string.password_length_error
    } else if (Pattern.compile(PASS_PATTERN).matcher(this).matches()) {
        R.string.password_error
    } else {
        null
    }
}

fun String.passwordMatches(repeated: String): Boolean {
    return this == repeated
}