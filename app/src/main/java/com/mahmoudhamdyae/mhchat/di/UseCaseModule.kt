package com.mahmoudhamdyae.mhchat.di

import com.mahmoudhamdyae.mhchat.domain.usecases.ValidateEmail
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidatePassword
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidateRepeatedPassword
import com.mahmoudhamdyae.mhchat.domain.usecases.ValidateUserName
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideUserNameUseCase(): ValidateUserName = ValidateUserName()

    @Provides
    fun provideEmailUseCase(): ValidateEmail = ValidateEmail()

    @Provides
    fun providePasswordUseCase(): ValidatePassword = ValidatePassword()

    @Provides
    fun provideRepeatedPasswordUseCase(): ValidateRepeatedPassword = ValidateRepeatedPassword()
}