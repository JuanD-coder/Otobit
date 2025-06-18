package com.juandev.otobit.data.local

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.juandev.otobit.domain.model.Audio
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentResolverHelper @Inject
constructor(@ApplicationContext val context: Context) {
    private var mCursor: Cursor? = null

    // Define las columnas que se recuperarán de MediaStore para cada archivo de audio.
    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
    )

    // Cláusula de selección para filtrar los resultados de la consulta.
    // Solo incluye archivos marcados como música (IS_MUSIC = 1)
    // y excluye ciertos tipos MIME (audio/amr, audio/3gpp, audio/aac) que suelen ser grabaciones o tonos.
    private var selectionClause: String? =
        "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.MIME_TYPE} NOT IN (?, ?, ?)"

    private var selectionArg = arrayOf("1", "audio/amr", "audio/3gpp", "audio/aac")
    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"


    /**
     * Obtiene la lista de archivos de audio del dispositivo.
     * Esta función está anotada con `@WorkerThread`, lo que indica que debe ejecutarse en un hilo de fondo
     * para no bloquear el hilo principal (UI), ya que la consulta al ContentResolver puede ser una operación larga.
     *
     * @return Una lista de objetos `Audio` que representan los archivos de música encontrados.
     */
    @WorkerThread
    fun getAudioData(): List<Audio> {
        return getCursorData()
    }

    // Función privada que realiza la consulta real al ContentResolver y procesa los resultados.
    private fun getCursorData(): MutableList<Audio> {
        val audioList = mutableListOf<Audio>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectionClause,
            selectionArg,
            sortOrder
        )

        // Utiliza el scope function 'use' para asegurar que el cursor se cierre automáticamente después de su uso.
        mCursor?.use { cursor ->
            val idColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val artistColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val dataColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)

            cursor.apply {
                if (count == 0) {
                    Log.e("Cursor", "getCursorData: Cursor is Empty")
                } else {
                    while (moveToNext()) {
                        val displayName = getString(displayNameColumn)
                        val id = getLong(idColumn)
                        val artist = getString(artistColumn)
                        val data = getString(dataColumn)
                        val duration = getInt(durationColumn)
                        val title = getString(titleColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, // URI base para archivos de audio externos.
                            id
                        )

                        audioList += Audio(
                            uri, title, displayName, artist, data, duration, id
                        )

                    }

                }
            }
        }

        return audioList
    }


}