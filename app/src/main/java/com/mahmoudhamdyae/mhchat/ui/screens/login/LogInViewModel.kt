package com.mahmoudhamdyae.mhchat.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.isValidEmail
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.home.HomeDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
): ChatViewModel(logService) {

    var uiState = mutableStateOf(LogInUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(navigate: (String) -> Unit) {
        if (checkAllFields()) {
            launchCatching {
                accountService.authenticate(email, password)
                navigate(HomeDestination.route)
            }
        }
    }

    fun onSignInWithGoogleClick(navigate: (String) -> Unit) {
        launchCatching {
//            accountService.authenticateWithGoogle(application)
//            navigate(HomeDestination.route)
        }
    }

    fun onForgotPasswordClick() {
        if (checkEmailValidation()) {
            launchCatching {
                accountService.sendRecoveryEmail(email)
                SnackBarManager.showMessage(R.string.recovery_email_sent)
            }
        }
    }

    private fun checkAllFields() = checkEmailValidation() && checkPasswordValidation()

    private fun checkEmailValidation(): Boolean {
        return if (email.isValidEmail()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.email_error)
            false
        }
    }

    private fun checkPasswordValidation(): Boolean {
        return if (password.isBlank()) {
            SnackBarManager.showMessage(R.string.empty_password_error)
            false
        } else {
            true
        }
    }
}