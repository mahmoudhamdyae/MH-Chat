package com.mahmoudhamdyae.mhchat.di

import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.ChatDatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
import com.mahmoudhamdyae.mhchat.domain.services.UsersDatabaseService
import com.mahmoudhamdyae.mhchat.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideUserNameUseCase() = ValidateUserName()

    @Provides
    fun provideEmailUseCase() = ValidateEmail()

    @Provides
    fun providePasswordUseCase() = ValidatePassword()

    @Provides
    fun provideRepeatedPasswordUseCase() = ValidateRepeatedPassword()

    @Provides
    fun provideLogInUseCase(accountService: AccountService) =
        LogInUseCase(accountService)

    @Provides
    fun provideSignUpUseCase(accountService: AccountService, usersDatabaseService: UsersDatabaseService) =
        SignUpUseCase(accountService, usersDatabaseService)

    @Provides
    fun provideForgotPasswordUseCase(accountService: AccountService) =
        ForgotPasswordUseCase(accountService)

    @Provides
    fun provideSignOutUseCase(accountService: AccountService) =
        SignOutUseCase(accountService)

    @Provides
    fun provideUpdateProfileUseCase(accountService: AccountService, usersDatabaseService: UsersDatabaseService, storageService: StorageService) =
        UpdateProfileUseCase(accountService, usersDatabaseService, storageService)

    @Provides
    fun provideGetMessagesUseCase(chatDatabaseService: ChatDatabaseService) =
        GetMessagesUseCase(chatDatabaseService)

    @Provides
    fun provideGetUsersUseCase(accountService: AccountService, usersDatabaseService: UsersDatabaseService) =
        GetUsersUseCase(accountService, usersDatabaseService)
}