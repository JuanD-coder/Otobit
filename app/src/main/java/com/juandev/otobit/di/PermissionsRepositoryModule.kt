package com.juandev.otobit.di

import com.juandev.otobit.data.local.repository.AppSettingsRepositoryImpl
import com.juandev.otobit.domain.repository.AppSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository

}