package com.juandev.otobit.domain.repository

import com.juandev.otobit.domain.model.PermissionsPreferenceState
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    fun getPermissionStatus(): Flow<PermissionsPreferenceState>

    suspend fun savePermissionStatus(state: PermissionsPreferenceState)

}