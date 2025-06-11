package com.juandev.otobit.domain.usecase.permission

import com.juandev.otobit.domain.model.PermissionsPreferenceState
import com.juandev.otobit.domain.repository.AppSettingsRepository
import javax.inject.Inject

class SavePermissionStatusUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {

    suspend operator fun invoke(state: PermissionsPreferenceState) {
        appSettingsRepository.savePermissionStatus(state)
    }

}