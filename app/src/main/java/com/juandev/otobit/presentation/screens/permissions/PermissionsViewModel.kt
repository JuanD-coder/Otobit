package com.juandev.otobit.presentation.screens.permissions

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandev.otobit.domain.model.PermissionsPreference
import com.juandev.otobit.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    val permissionsStatus: StateFlow<PermissionsPreference> =
        appSettingsRepository.getPermissionStatus()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PermissionsPreference()
            )

    fun updateAllPermissionsStatus(granted: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.setPermissionStatus(granted)

        }
    }

    fun updateMediaPermissionStatus(granted: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.setMediaPermissionStatus(granted)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                // Si legacyRead usa la misma lógica o una clave diferente pero relacionada
                // appSettingsRepository.setLegacyReadStatus(granted)
            }
        }
    }

    fun updateFolderAccessStatus(granted: Boolean) {
        viewModelScope.launch {
            appSettingsRepository.setFolderAccessStatus(granted)
        }
    }

    fun updateLegacyReadStatus(granted: Boolean) {
        viewModelScope.launch {
            val current = permissionsStatus.value
            appSettingsRepository.setStorageAccessStatus(granted)
        }
    }
}