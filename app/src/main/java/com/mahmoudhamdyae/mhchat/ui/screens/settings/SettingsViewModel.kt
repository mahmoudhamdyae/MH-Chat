package com.mahmoudhamdyae.mhchat.ui.screens.settings

import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.usecases.BaseUseCase
import com.mahmoudhamdyae.mhchat.ui.screens.ChatViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: BaseUseCase,
    logService: LogService
): ChatViewModel(logService) {

    fun onDeleteAccount(password: String, navigate: (String) -> Unit) {
        launchCatching {
            useCase.deleteAccountUseCase(password, navigate)
        }
    }
}