package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val accountService: AccountService
) {

    suspend operator fun invoke() {
        accountService.signOut()
    }
}