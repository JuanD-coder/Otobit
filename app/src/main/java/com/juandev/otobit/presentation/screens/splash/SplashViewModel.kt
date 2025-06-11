package com.juandev.otobit.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandev.otobit.domain.model.PermissionsPreferenceState
import com.juandev.otobit.domain.usecase.permission.GetPermissionStatusUseCase
import com.juandev.otobit.domain.usecase.permission.SavePermissionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getPermissionStatusUseCase: GetPermissionStatusUseCase,
    private val savePermissionStatusUseCase: SavePermissionStatusUseCase,
): ViewModel() {

    val permissionsState: StateFlow<PermissionsPreferenceState> =
        getPermissionStatusUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PermissionsPreferenceState() // valores por defecto (false, false, false)
            )

    fun updatePermissionState(newState: PermissionsPreferenceState) {
        viewModelScope.launch {
            savePermissionStatusUseCase(newState)
        }

    }

}