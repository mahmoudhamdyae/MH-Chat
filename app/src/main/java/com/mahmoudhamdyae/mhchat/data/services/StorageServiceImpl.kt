package com.mahmoudhamdyae.mhchat.data.services

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
import com.mahmoudhamdyae.mhchat.domain.services.trace
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    storage: FirebaseStorage,
    private val accountService: AccountService
): StorageService {

    private val profileImagesRef = storage.reference.child(IMAGE_STORAGE_REF)

    override suspend fun saveImage(imageUri: Uri) {
        trace(UPLOAD_IMAGE_TRACE) {
            profileImagesRef.child(accountService.currentUserId + ".jpg")
                .putFile(imageUri).await()
        }
    }

    override suspend fun getImage(userId: String): Uri? {
        return profileImagesRef.child("$userId.jpg").downloadUrl.await()
    }

    companion object {
        private const val IMAGE_STORAGE_REF = "images"
        private const val UPLOAD_IMAGE_TRACE = "upload_image"
    }
}