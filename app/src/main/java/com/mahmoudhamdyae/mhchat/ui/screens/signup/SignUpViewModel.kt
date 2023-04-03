package com.mahmoudhamdyae.mhchat.ui.screens.signup

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.ext.isValidEmail
import com.mahmoudhamdyae.mhchat.common.ext.isValidPassword
import com.mahmoudhamdyae.mhchat.common.ext.isValidUserName
import com.mahmoudhamdyae.mhchat.common.ext.passwordMatches
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: UsersDatabaseService,
    private val storageService: StorageService,
    logService: LogService
): ChatViewModel(logService) {

    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val userName
        get() = uiState.value.userName
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onUserNameChange(newValue: String) {
        uiState.value = uiState.value.copy(userName = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(navigate: (String) -> Unit) {
        if (validateAllFields()) {
            launchCatching {
                accountService.linkAccount(email, password)
                databaseService.saveUser(
                    User(
                        userId = accountService.currentUserId,
                        email = email,
                        userName = userName
                    )
                )
                navigate(ProfileImageDestination.route)
            }
        }
    }

    private fun validateAllFields(): Boolean {
        return validateUserName()
                && validateEmail()
                && validatePassword()
                && validateRepeatPassword()
    }

    private fun validateUserName(): Boolean {
        return if (userName.isValidUserName()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.user_name_error)
            false
        }
    }

    private fun validateEmail(): Boolean {
        return if (email.isValidEmail()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.email_error)
            false
        }
    }

    private fun validatePassword(): Boolean {
        return if (password.isValidPassword()) {
            true
        } else {
            SnackBarManager.showMessage(R.string.password_error)
            false
        }
    }

    private fun validateRepeatPassword(): Boolean {
        return if (password.passwordMatches(uiState.value.repeatPassword)) {
            true
        } else {
            SnackBarManager.showMessage(R.string.password_match_error)
            false
        }
    }

    fun saveProfileImage(imageUri: Uri) {
        launchCatching {
            storageService.saveImage(imageUri)
            databaseService.
                updateProfileImage(storageService.getImage(accountService.currentUserId).toString())
        }
    }
}