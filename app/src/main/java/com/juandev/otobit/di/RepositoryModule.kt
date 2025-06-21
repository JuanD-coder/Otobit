package com.juandev.otobit.di

import com.juandev.otobit.data.repository.AppSettingsRepositoryImpl
import com.juandev.otobit.data.repository.SongRepositoryImpl
import com.juandev.otobit.domain.repository.AppSettingsRepository
import com.juandev.otobit.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository

    @Binds
    @Singleton
    abstract fun bindSongRepository(
        songRepositoryImpl: SongRepositoryImpl
    ): SongRepository

}