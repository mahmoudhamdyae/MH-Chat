package com.mahmoudhamdyae.mhchat.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun auth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage

    @Provides
    fun provideFCM(): FirebaseMessaging = FirebaseMessaging.getInstance()
}