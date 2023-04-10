package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val accountService: AccountService
) {

    suspend operator fun invoke(email: String, password: String) {
        accountService.authenticate(email, password)
    }
}