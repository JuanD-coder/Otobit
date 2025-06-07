package com.juandev.otobit.data.local.repository

import com.juandev.otobit.data.local.AppSettingsLocalDataSource
import com.juandev.otobit.domain.model.PermissionsPreference
import com.juandev.otobit.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsRepositoryImpl @Inject constructor(
    private val localDataSource: AppSettingsLocalDataSource
) : AppSettingsRepository {

    override fun getPermissionStatus(): Flow<PermissionsPreference> {
        return localDataSource.arePermissionsGranted
    }

    override suspend fun setPermissionStatus(granted: Boolean) {
        localDataSource.savePermissionsGrantedStatus(granted)
    }

    override suspend fun setMediaPermissionStatus(granted: Boolean) {
        localDataSource.saveMediaPermissionStatus(granted)
    }

    override suspend fun setFolderAccessStatus(granted: Boolean) {
        localDataSource.saveFolderAccessStatus(granted)
    }

    override suspend fun setStorageAccessStatus(granted: Boolean) {
        localDataSource.saveLegacyReadStatus(granted)
    }
}