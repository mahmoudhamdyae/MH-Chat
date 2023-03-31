package com.mahmoudhamdyae.mhchat.di

import com.mahmoudhamdyae.mhchat.data.services.AccountServiceImpl
import com.mahmoudhamdyae.mhchat.data.services.DatabaseServiceImpl
import com.mahmoudhamdyae.mhchat.data.services.LogServiceImpl
import com.mahmoudhamdyae.mhchat.data.services.StorageServiceImpl
import com.mahmoudhamdyae.mhchat.domain.services.AccountService
import com.mahmoudhamdyae.mhchat.domain.services.DatabaseService
import com.mahmoudhamdyae.mhchat.domain.services.LogService
import com.mahmoudhamdyae.mhchat.domain.services.StorageService
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
    abstract fun provideDatabaseService(impl: DatabaseServiceImpl): DatabaseService

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService
}