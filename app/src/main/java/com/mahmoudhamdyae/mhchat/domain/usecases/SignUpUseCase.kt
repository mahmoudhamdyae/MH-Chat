package com.mahmoudhamdyae.mhchat.domain.usecases

import com.mahmoudhamdyae.mhchat.domain.models.User
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService
) {

    suspend operator fun invoke(userName: String, email: String, password: String) {
        accountService.linkAccount(email, password)
        usersDatabaseService.saveUser(
            User(
                userId = accountService.currentUserId,
                email = email,
                userName = userName
            )
        )
    }
}