package com.juandev.otobit.domain.usecase.permission

import com.juandev.otobit.domain.repository.AppSettingsRepository
import javax.inject.Inject

class SetPermissionGrantedUseCase @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository
) {
    suspend operator fun invoke(granted: Boolean) {
        appSettingsRepository.setPermissionStatus(granted)
    }
}