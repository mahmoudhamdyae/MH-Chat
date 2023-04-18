package com.mahmoudhamdyae.mhchat.domain.usecases

import android.net.Uri
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService

class UpdateProfileUseCase (
    private val accountService: AccountService,
    private val usersDatabaseService: UsersDatabaseService,
    private val storageService: StorageService
) {

    suspend operator fun invoke(
        imageUri: Uri? = null,
        userName: String? = null,
        bio: String? = null,
    ) {
        if (imageUri != null) {
            storageService.saveImage(imageUri)
            usersDatabaseService.
                updateProfileImage(storageService.getImage(accountService.currentUserId).toString())
        }

        if (bio != null) {
            usersDatabaseService.updateBio(bio)
        }

        if (userName != null) {
            usersDatabaseService.updateUserName(userName)
        }
    }
}