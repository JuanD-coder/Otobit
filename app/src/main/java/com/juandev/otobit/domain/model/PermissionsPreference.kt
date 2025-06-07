package com.juandev.otobit.domain.model

data class PermissionsPreference(
    val mediaPermission: Boolean = false,
    val folderAccess: Boolean = false,
    val legacyRead: Boolean = false
)
