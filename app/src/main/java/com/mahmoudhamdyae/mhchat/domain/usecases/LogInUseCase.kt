package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.services.AccountService

class LogInUseCase (
    private val accountService: AccountService
) {

    suspend operator fun invoke(email: String, password: String) {
        accountService.authenticate(email, password)
    }
}