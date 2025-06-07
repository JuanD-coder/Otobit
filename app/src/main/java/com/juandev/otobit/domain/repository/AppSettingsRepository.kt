package com.juandev.otobit.domain.repository

import com.juandev.otobit.domain.model.PermissionsPreference
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    fun getPermissionStatus(): Flow<PermissionsPreference>

    suspend fun setPermissionStatus(granted: Boolean)

    suspend fun setMediaPermissionStatus(granted: Boolean)

    suspend fun setFolderAccessStatus(granted: Boolean)

    suspend fun setStorageAccessStatus(granted: Boolean)
}