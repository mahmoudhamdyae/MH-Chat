package com.mahmoudhamdyae.mhchat.ui.screens.auth

sealed class ValidationEvent {
    object Success: ValidationEvent()
}