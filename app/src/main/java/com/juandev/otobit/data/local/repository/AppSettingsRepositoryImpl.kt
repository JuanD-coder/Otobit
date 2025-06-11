package com.juandev.otobit.data.local.repository

import com.juandev.otobit.data.local.datastore.AppSettingsLocalDataSource
import com.juandev.otobit.domain.model.PermissionsPreferenceState
import com.juandev.otobit.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsRepositoryImpl @Inject constructor(
    private val localDataSource: AppSettingsLocalDataSource
) : AppSettingsRepository {

    override fun getPermissionStatus(): Flow<PermissionsPreferenceState> {
        return localDataSource.permissionsState
    }

    override suspend fun savePermissionStatus(state: PermissionsPreferenceState) {
        localDataSource.savePermissionsGrantedStatus(state)
    }

}