package com.juandev.otobit.data.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

internal object PreferencesKeys {
    val PERMISSIONS_READ_MEDIA_AUDIO = booleanPreferencesKey("permissions_read_media_audio")
    val PERMISSIONS_ACTION_OPEN_DOCUMENT_TREE = booleanPreferencesKey("permissions_open_document_tree")
    // Para compatibilidad con Android < 13
    val PERMISSIONS_READ_EXTERNAL_STORAGE = booleanPreferencesKey("permissions_read_external_storage")

}

