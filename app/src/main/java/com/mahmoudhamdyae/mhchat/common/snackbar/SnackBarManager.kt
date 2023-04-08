package com.mahmoudhamdyae.mhchat.common.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SnackBarManager {
    private val messages: MutableSharedFlow<SnackBarMessage?> = MutableSharedFlow()
    val snackBarMessages: SharedFlow<SnackBarMessage?>
        get() = messages.asSharedFlow()

    suspend fun showMessage(@StringRes message: Int) {
        messages.emit(SnackBarMessage.ResourceSnackBar(message))
    }

    suspend fun showMessage(message: SnackBarMessage) {
        messages.emit(message)
    }
}