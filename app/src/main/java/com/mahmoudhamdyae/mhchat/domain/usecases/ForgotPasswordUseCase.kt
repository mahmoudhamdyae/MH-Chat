package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.services.AccountService

class ForgotPasswordUseCase (
    private val accountService: AccountService
) {

    suspend operator fun invoke(email: String) {
        accountService.sendRecoveryEmail(email)
    }
}