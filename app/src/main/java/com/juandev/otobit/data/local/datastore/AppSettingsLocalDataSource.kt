package com.juandev.otobit.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.juandev.otobit.domain.model.PermissionsPreferenceState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class AppSettingsLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    internal object PreferencesKeys {
        val PERMISSIONS_READ_MEDIA_AUDIO = booleanPreferencesKey("permissions_read_media_audio")
        val PERMISSIONS_NOTIFICATION = booleanPreferencesKey("permissions_notification")
        val PERMISSIONS_READ_EXTERNAL_STORAGE = booleanPreferencesKey("permissions_read_external_storage") // Para compatibilidad con Android < 13
    }

    val permissionsState: Flow<PermissionsPreferenceState> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences()) // Emite preferencias vacías en caso de error
            } else {
                throw exception // Vuelve a lanzar otras excepciones
            }
        }
        .map { preferences ->
            PermissionsPreferenceState(
                mediaPermission = preferences[PreferencesKeys.PERMISSIONS_READ_MEDIA_AUDIO]
                    ?: false,
                notificationsPermission = preferences[PreferencesKeys.PERMISSIONS_NOTIFICATION]
                    ?: false,
                legacyRead = preferences[PreferencesKeys.PERMISSIONS_READ_EXTERNAL_STORAGE]
                    ?: false,
            )
        }

    suspend fun savePermissionsGrantedStatus(state: PermissionsPreferenceState) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PERMISSIONS_READ_MEDIA_AUDIO] = state.mediaPermission
            preferences[PreferencesKeys.PERMISSIONS_NOTIFICATION] = state.notificationsPermission
            preferences[PreferencesKeys.PERMISSIONS_READ_EXTERNAL_STORAGE] = state.legacyRead
        }
    }

}