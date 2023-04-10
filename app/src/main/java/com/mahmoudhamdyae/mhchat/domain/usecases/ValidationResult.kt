package com.mahmoudhamdyae.mhchat.domain.usecases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: Int? = null
)