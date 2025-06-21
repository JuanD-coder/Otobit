package com.juandev.otobit.presentation.screens.allsongs

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.juandev.otobit.core.player.service.JetAudioServiceHandler
import com.juandev.otobit.core.player.service.JetAudioState
import com.juandev.otobit.core.player.service.PlayerEvent
import com.juandev.otobit.domain.model.SongData
import com.juandev.otobit.domain.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private val EMPTY_SONG_DATA = SongData(
    "".toUri(), "", "", "", "", 0, 0L
)

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AllSongsViewModel @Inject constructor(
    private val audioServiceHandler: JetAudioServiceHandler,
    private val repository: SongRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var duration by savedStateHandle.saveable { mutableLongStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableFloatStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSelectedSong by savedStateHandle.saveable { mutableStateOf(EMPTY_SONG_DATA) }
    var songsList by savedStateHandle.saveable { mutableStateOf(listOf<SongData>()) }

    private val _uiState: MutableStateFlow<UIState> = MutableStateFlow(UIState.Initial)
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        loadSongData()
    }

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    JetAudioState.Initial -> _uiState.value = UIState.Initial
                    is JetAudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.CurrentPlaying -> {
                        currentSelectedSong = songsList[mediaState.mediaItemIndex]
                    }

                    is JetAudioState.Playing -> isPlaying = mediaState.isPlaying
                    is JetAudioState.PlayingState -> TODO()
                    is JetAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UIState.Ready
                    }
                }
            }
        }
    }

    fun onUIEvents(uiEvent: UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            UIEvent.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvent.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvent.SeekToNext -> audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvent.PlayPause -> {
                audioServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
            }

            is UIEvent.SeekTo -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = ((duration * uiEvent.progress) / 100f).toLong()
                )
            }

            is UIEvent.SelectedAudioChange -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvent.index
                )
            }

            is UIEvent.UpdateProgress -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvent.newProgress
                    )
                )
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minutes) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun calculateProgressValue(currentProgress: Long) {
        progress =
            if (currentProgress > 0)
                ((currentProgress.toFloat() / duration.toFloat()) * 100f)
            else 0f

        progressString = formatDuration(currentProgress)
    }

    private fun loadSongData() {
        viewModelScope.launch {
            val song = repository.getSongData()
            songsList = song
            setMediaItemList()
        }
    }

    private fun setMediaItemList() {
        songsList.map { songData ->
            MediaItem.Builder()
                .setUri(songData.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(songData.artist)
                        .setDisplayTitle(songData.title)
                        .setSubtitle(songData.displayName)
                        .build()
                )
                .build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

}

sealed class UIEvent {
    object PlayPause : UIEvent()
    data class SelectedAudioChange(val index: Int) : UIEvent()
    data class SeekTo(val progress: Float) : UIEvent()
    object SeekToNext : UIEvent()
    object Backward : UIEvent()
    object Forward : UIEvent()
    data class UpdateProgress(val newProgress: Float) : UIEvent()
}

sealed class UIState {
    object Initial : UIState()
    object Ready : UIState()
}