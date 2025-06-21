package com.juandev.otobit.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongData (
    val uri: Uri,
    val title: String,
    val displayName: String,
    val artist: String,
    val data: String,
    val duration: Int,
    val id: Long,
): Parcelable