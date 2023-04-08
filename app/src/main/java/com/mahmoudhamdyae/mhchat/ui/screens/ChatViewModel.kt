package com.mahmoudhamdyae.mhchat.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarManager
import com.mahmoudhamdyae.mhchat.common.snackbar.SnackBarMessage.Companion.toSnackBarMessage
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ChatViewModel @Inject constructor(
    private val logService: LogService
): ViewModel() {

    fun launchCatching(snackBar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackBar) {
                    viewModelScope.launch {
                        SnackBarManager.showMessage(throwable.toSnackBarMessage())
                    }
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block
        )
}