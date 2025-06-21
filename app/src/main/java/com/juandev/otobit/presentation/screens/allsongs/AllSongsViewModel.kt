package com.juandev.otobit.presentation.screens.allsongs

import android.annotation.SuppressLint
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class AllSongsViewModel @Inject constructor(
    private val audioServiceHandler: JetAudioServiceHandler,
    private val songRepository: SongRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllSongsScreenUiState())
    val uiState: StateFlow<AllSongsScreenUiState> = _uiState.asStateFlow()


    init {
        loadSongData()
    }

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest { mediaState ->
                when (mediaState) {
                    JetAudioState.Initial -> _uiState.update { it.copy(loadState = ScreenLoadState.Initial) }
                    is JetAudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.CurrentPlaying -> {
                        val currentSong =
                            _uiState.value.songsList.getOrNull(mediaState.mediaItemIndex)
                        _uiState.update {
                            it.copy(
                                currentSelectedSong = currentSong ?: EMPTY_SONG_DATA,
                                currentPlayingIndex = mediaState.mediaItemIndex
                            )
                        }
                    }

                    is JetAudioState.Playing -> _uiState.update { it.copy(isPlaying = mediaState.isPlaying) }
                    is JetAudioState.PlayingState -> TODO()
                    is JetAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.Ready -> {
                        _uiState.update {
                            it.copy(
                                duration = mediaState.duration,
                                loadState = ScreenLoadState.Ready
                            )
                        }
                    }
                }
            }
        }
    }

    fun onUIEvents(uiEvent: UIEvents) = viewModelScope.launch {
        when (uiEvent) {
            UIEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.SeekToNext -> audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvents.PlayPause -> {
                audioServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
            }

            is UIEvents.SeekTo -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = ((_uiState.value.duration * uiEvent.progress) / 100f).toLong()
                )
            }

            is UIEvents.SelectedAudioChange -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvent.index
                )
            }

            is UIEvents.UpdateProgress -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.UpdateProgress(
                        uiEvent.newProgress
                    )
                )
                _uiState.update { it.copy(progress = uiEvent.newProgress) }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds = (minutes) - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES)

        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCleared() {
        viewModelScope.launch {
            audioServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }

    private fun calculateProgressValue(currentProgress: Long) {
        val duration = _uiState.value.duration
        val progressFraction = if (duration > 0)
            ((currentProgress.toFloat() / duration.toFloat()) * 100f)
        else
            0f

        _uiState.update {
            it.copy(
                currentPosition = currentProgress,
                progress = progressFraction,
                progressString = formatDuration(currentProgress)
            )
        }
    }

    private fun loadSongData() {
        viewModelScope.launch {
            _uiState.update { it.copy(loadState = ScreenLoadState.Loading) }
            try {
                val songs = songRepository.getSongData()
                _uiState.update {
                    it.copy(
                        songsList = songs,
                        loadState = if (songs.isEmpty()) ScreenLoadState.Error("No songs found") else ScreenLoadState.Ready
                    )
                }
                if (songs.isNotEmpty()) {
                    setMediaItemList(songs)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadState = ScreenLoadState.Error("Failed to load songs: ${e.message}")
                    )
                }
            }
        }
    }

    private fun setMediaItemList(songs: List<SongData>) {
        songs.map { songData ->
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

private val EMPTY_SONG_DATA = SongData(
    "".toUri(), "", "", "", "", 0, 0L
)

data class AllSongsScreenUiState(
    val loadState: ScreenLoadState = ScreenLoadState.Initial,
    val duration: Long = 0L,
    val currentPosition: Long = 0L, // Para el progreso actual en milisegundos
    val progress: Float = 0f,
    val progressString: String = "00:00",
    val isPlaying: Boolean = false,
    val currentSelectedSong: SongData = EMPTY_SONG_DATA,
    val songsList: List<SongData> = emptyList(),
    val currentPlayingIndex: Int = -1 // Para saber qué canción de la lista se está reproduciendo
)

sealed class UIEvents {
    object PlayPause : UIEvents()
    data class SelectedAudioChange(val index: Int) : UIEvents()
    data class SeekTo(val progress: Float) : UIEvents()
    object SeekToNext : UIEvents()
    object Backward : UIEvents()
    object Forward : UIEvents()
    data class UpdateProgress(val newProgress: Float) : UIEvents()
}

sealed class ScreenLoadState {
    object Initial : ScreenLoadState()
    object Loading : ScreenLoadState()
    object Ready : ScreenLoadState()
    data class Error(val message: String?) : ScreenLoadState()
}