package com.mahmoudhamdyae.mhchat.domain.services

import android.net.Uri

interface StorageService {

    // Users Profile Image
    suspend fun saveImage(imageUri: Uri)
    suspend fun getImage(userId: String): Uri?
    suspend fun delImage(userId: String)
}