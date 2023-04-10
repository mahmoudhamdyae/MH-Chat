package com.mahmoudhamdyae.mhchat.di

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.usecases.*
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

    @Provides
    fun provideLogInUseCase(accountService: AccountService): LogInUseCase =
        LogInUseCase(accountService)

    @Provides
    fun provideSignUpUseCase(accountService: AccountService): SignUpUseCase =
        SignUpUseCase(accountService)

    @Provides
    fun provideForgotPasswordUseCase(accountService: AccountService): ForgotPasswordUseCase =
        ForgotPasswordUseCase(accountService)

    @Provides
    fun provideSignOutUseCase(accountService: AccountService): SignOutUseCase =
        SignOutUseCase(accountService)
}