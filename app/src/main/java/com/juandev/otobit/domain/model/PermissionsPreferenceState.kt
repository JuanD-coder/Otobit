package com.juandev.otobit.domain.model

data class PermissionsPreferenceState(
    val mediaPermission: Boolean = false,
    val notificationsPermission: Boolean = false,
    val legacyRead: Boolean = false // Opcional para apis <= 32
)
