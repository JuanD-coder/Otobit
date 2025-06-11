package com.juandev.otobit.di

import android.content.Context
import com.juandev.otobit.data.local.datastore.AppSettingsLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStorageModule {

    @Provides
    @Singleton
    fun provideAppSettingsLocalDataSource(@ApplicationContext appContext: Context): AppSettingsLocalDataSource {
        return AppSettingsLocalDataSource(appContext)
    }

}