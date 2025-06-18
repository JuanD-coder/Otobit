package com.juandev.otobit.presentation.screens.player

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
): ViewModel() {

    fun prepareExoPlayer(uri: Uri) {
        exoPlayer.setMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun pauseExoPlayer() = exoPlayer.pause()

    fun stopExoPlayer() = exoPlayer.stop()

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }

}