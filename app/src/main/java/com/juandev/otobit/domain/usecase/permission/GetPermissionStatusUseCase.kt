package com.juandev.otobit.domain.usecase.permission

import com.juandev.otobit.domain.model.PermissionsPreference
import com.juandev.otobit.domain.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPermissionStatusUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    operator fun invoke(): Flow<PermissionsPreference> {
        return appSettingsRepository.getPermissionStatus()
    }
}