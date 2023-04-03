package com.mahmoudhamdyae.mhchat.di

import com.mahmoudhamdyae.mhchat.data.services.*
import com.mahmoudhamdyae.mhchat.domain.services.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds
    abstract fun provideUsersDatabaseService(impl: UsersDatabaseServiceImpl): UsersDatabaseService

    @Binds
    abstract fun provideChatDatabaseService(impl: ChatDatabaseServiceImpl): ChatDatabaseService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}