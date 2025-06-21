package com.juandev.otobit.core.player.service

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class JetAudioServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer,
) : Player.Listener {

    private val _audioState: MutableStateFlow<JetAudioState> =
        MutableStateFlow(JetAudioState.Initial)
    val audioState: StateFlow<JetAudioState> = _audioState.asStateFlow()

    private var job: Job? = null

    init {
        exoPlayer.addListener(this)
    }

    fun addMediaItem(mediaItem: MediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems: List<MediaItem>) {
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudioIndex: Int = -1,
        seekPosition: Long = 0
    ) {
        when (playerEvent) {
            PlayerEvent.Backward -> exoPlayer.seekBack()
            PlayerEvent.Forward -> exoPlayer.seekForward()
            PlayerEvent.SeekToNext -> exoPlayer.seekToNext()
            PlayerEvent.PlayPause -> togglePlayOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            PlayerEvent.SelectedAudioChange -> {
                when (selectedAudioIndex) {
                    exoPlayer.currentMediaItemIndex -> {
                        togglePlayOrPause()
                    }

                    else -> {
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                        _audioState.value = JetAudioState.Playing(
                            isPlaying = true
                        )
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }

            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> {
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }
        }
    }

    @OptIn(UnstableApi::class)
    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_BUFFERING -> _audioState.value =
                JetAudioState.Buffering(exoPlayer.currentPosition)

            ExoPlayer.STATE_READY -> _audioState.value =
                JetAudioState.Ready(exoPlayer.duration)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = JetAudioState.Playing(isPlaying = isPlaying)
        _audioState.value = JetAudioState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    @OptIn(UnstableApi::class)
    private suspend fun togglePlayOrPause() {
        if (exoPlayer.isPlaying) {
            exoPlayer.pause()
            stopProgressUpdate()
        } else {
            exoPlayer.play()
            _audioState.value = JetAudioState.Playing(
                isPlaying = true // Considera si este estado es redundante dado onIsPlayingChanged
            )
            // No es necesario iniciar explícitamente startProgressUpdate aquí si onIsPlayingChanged ya lo hace.
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            delay(500)
            _audioState.value =
                JetAudioState.Progress( // Asegúrate de que este estado sea útil y no cause demasiadas recomposiciones
                    progress = exoPlayer.currentPosition
                )
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
        _audioState.value =
            JetAudioState.Playing(isPlaying = false) // Considera si este estado es redundante
    }

}

sealed class PlayerEvent {
    object PlayPause : PlayerEvent()
    object SelectedAudioChange : PlayerEvent()
    object Backward : PlayerEvent()
    object Forward : PlayerEvent()
    object Stop : PlayerEvent()
    object SeekToNext : PlayerEvent()
    object SeekTo : PlayerEvent() // SeekToPrevious
    data class UpdateProgress(val newProgress: Float) : PlayerEvent()
}

sealed class JetAudioState {
    object Initial : JetAudioState()
    data class Ready(val duration: Long) : JetAudioState()
    data class Progress(val progress: Long) : JetAudioState()
    data class Buffering(val progress: Long) : JetAudioState()
    data class Playing(val isPlaying: Boolean) : JetAudioState()

    // Considera un estado más completo para PlayingState
    data class PlayingState(
        val isPlaying: Boolean,
        val currentMediaItemIndex: Int,
        val currentPosition: Long = 0,
        val duration: Long = 0
    ) : JetAudioState()

    data class CurrentPlaying(val mediaItemIndex: Int) : JetAudioState()

}