package com.mahmoudhamdyae.mhchat.ui.screens.settings

import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.mhchat.R
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import com.mahmoudhamdyae.mhchat.ui.screens.login.LogInDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: UsersDatabaseService,
    private val storageService: StorageService,
    logService: LogService
): ChatViewModel(logService) {

    private lateinit var currentUserEmail: String

    fun initialize() {
        viewModelScope.launch {
            currentUserEmail = accountService.currentUserEmail
        }
    }

    fun onDeleteAccount(password: String, navigate: (String) -> Unit) {
        launchCatching {
            if (validatePassword(password)) {
                accountService.deleteAccount(currentUserEmail, password)
                navigate(LogInDestination.route)
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isBlank()) {
            SnackBarManager.showMessage(R.string.empty_password_error)
            false
        } else {
            true
        }
    }
}