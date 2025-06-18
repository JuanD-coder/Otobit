package com.juandev.otobit.domain.model

import android.net.Uri

data class Audio (
    val uri: Uri,
    val title: String,
    val displayName: String,
    val artist: String,
    val data: String,
    val duration: Int,
    val id: Long,
)