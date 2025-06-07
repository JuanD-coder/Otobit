package com.juandev.otobit.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.juandev.otobit.data.local.datastore.PreferencesKeys
import com.juandev.otobit.domain.model.PermissionsPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsLocalDataSource @Inject constructor(
    private val dataSource: DataStore<Preferences>
) {

    val arePermissionsGranted: Flow<PermissionsPreference> = dataSource.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences()) // Emite preferencias vacías en caso de error
            } else {
                throw exception // Vuelve a lanzar otras excepciones
            }
        }
        .map { preferences ->
            PermissionsPreference(
                mediaPermission = preferences[PreferencesKeys.PERMISSIONS_READ_MEDIA_AUDIO]
                    ?: false,
                folderAccess = preferences[PreferencesKeys.PERMISSIONS_ACTION_OPEN_DOCUMENT_TREE]
                    ?: false,
                legacyRead = preferences[PreferencesKeys.PERMISSIONS_READ_EXTERNAL_STORAGE]
                    ?: false,
            )
        }

    /**
     * Guarda el estado de concesión de permisos.
     * @param granted El nuevo estado de los permisos (true si concedidos, false si no).
     */
    suspend fun savePermissionsGrantedStatus(granted: Boolean) {
        dataSource.edit { preferences ->
            preferences[PreferencesKeys.PERMISSIONS_READ_MEDIA_AUDIO] = granted
            preferences[PreferencesKeys.PERMISSIONS_ACTION_OPEN_DOCUMENT_TREE] = granted
            preferences[PreferencesKeys.PERMISSIONS_READ_EXTERNAL_STORAGE] = granted
        }
    }

    suspend fun saveMediaPermissionStatus(granted: Boolean) {
        dataSource.edit { preferences ->
            preferences[PreferencesKeys.PERMISSIONS_READ_MEDIA_AUDIO] = granted
        }
    }

    suspend fun saveFolderAccessStatus(granted: Boolean) {
        dataSource.edit { preferences ->
            preferences[PreferencesKeys.PERMISSIONS_ACTION_OPEN_DOCUMENT_TREE] = granted
        }
    }

    suspend fun saveLegacyReadStatus(granted: Boolean) {
        dataSource.edit { preferences ->
            preferences[PreferencesKeys.PERMISSIONS_READ_EXTERNAL_STORAGE] = granted
        }
    }

}