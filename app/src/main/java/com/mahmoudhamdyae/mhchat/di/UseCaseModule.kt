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
    fun provideUseCase(
        accountService: AccountService,
        usersDatabaseService: UsersDatabaseService,
        chatDatabaseService: ChatDatabaseService,
        storageService: StorageService,
    ): BaseUseCase {
        return BaseUseCase(
            deleteAccountUseCase = DeleteAccountUseCase(accountService, usersDatabaseService, chatDatabaseService),
            forgotPasswordUseCase = ForgotPasswordUseCase(accountService),
            getChatsUseCase = GetChatsUseCase(usersDatabaseService, chatDatabaseService),
            getMessagesUseCase = GetMessagesUseCase(chatDatabaseService),
            getUsersUseCase = GetUsersUseCase(accountService, usersDatabaseService),
            getUserUseCase = GetUserUseCase(usersDatabaseService),
            logInUseCase = LogInUseCase(accountService),
            sendMessageUseCase = SendMessageUseCase(chatDatabaseService),
            signOutUseCase = SignOutUseCase(accountService),
            signUpUseCase = SignUpUseCase(accountService, usersDatabaseService),
            updateProfileUseCase = UpdateProfileUseCase(accountService, usersDatabaseService, storageService),
            validateEmail = ValidateEmail(),
            validatePassword = ValidatePassword(),
            validateRepeatedPassword = ValidateRepeatedPassword(),
            validateUserName = ValidateUserName(),
        )
    }
}