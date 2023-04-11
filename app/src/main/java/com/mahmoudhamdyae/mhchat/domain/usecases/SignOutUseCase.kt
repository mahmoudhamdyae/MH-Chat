package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.services.AccountService

class SignOutUseCase (
    private val accountService: AccountService
) {

    suspend operator fun invoke() {
        accountService.signOut()
    }
}