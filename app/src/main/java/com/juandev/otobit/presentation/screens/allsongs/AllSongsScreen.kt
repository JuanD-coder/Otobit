package com.juandev.otobit.presentation.screens.allsongs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.juandev.otobit.R
import com.juandev.otobit.domain.model.SongData
import com.juandev.otobit.presentation.ui.theme.OtobitTheme
import kotlin.math.floor

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPrev() {
    OtobitTheme {
        AllSongsScreen(
            progress = 50f,
            onProgress = {},
            isSongPlaying = true,
            songList = listOf(
                SongData("".toUri(), "Title One", "Said", "Title One", "", 0, 0L),
                SongData("".toUri(), "Title two", "Unknown", "Title two", "", 0, 0L),
            ),
            currentPlayingSong = SongData(
                "".toUri(),
                "Title One",
                "Unknown",
                "Title two",
                "",
                0,
                0L,
            ),
            onStart = {},
            onItemClick = {},
            onNext = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllSongsScreen(
    progress: Float,
    onProgress: (Float) -> Unit,
    isSongPlaying: Boolean,
    currentPlayingSong: SongData,
    songList: List<SongData>,
    onStart: () -> Unit,
    onItemClick: (Int) -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomBarPlayer(
                progress = progress,
                onProgress = onProgress,
                song = currentPlayingSong,
                isSongPlaying = isSongPlaying,
                onStart = onStart,
                onNext = onNext
            )
        }
    ) {
        /*Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AllSongs()
        }*/

        LazyColumn(
            contentPadding = it
        ) {
            itemsIndexed(songList) { index, song ->
                SongItem(
                    song = song,
                    onItemClick = { onItemClick(index) }
                )
            }
        }

    }
}

@Composable
fun AllSongs() {
    Text(text = "Songs Screen")
}

@Composable
fun SongItem(
    song: SongData,
    onItemClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                onItemClick()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = song.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }
            Text(
                text = timeStampToDuration(song.duration.toLong())
            )
            Spacer(modifier = Modifier.size(8.dp))
        }

    }
}

@Composable
fun BottomBarPlayer(
    progress: Float,
    onProgress: (Float) -> Unit,
    song: SongData,
    isSongPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit
) {
    BottomAppBar(
        content = {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ArtistInfo(
                        song = song,
                        modifier = Modifier.weight(1f),
                    )
                    MediaPlayerController(
                        isSongPlaying = isSongPlaying,
                        onStart = onStart,
                        onNext = onNext
                    )
                    Slider(
                        value = progress,
                        onValueChange = { onProgress(it) },
                        valueRange = 0f..100f
                    )

                }
            }
        }
    )
}

@Composable
fun ArtistInfo(song: SongData, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerIconItem(
            icon = painterResource(R.drawable.ic_music_note),
            borderStroke = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        ) {}
        Spacer(modifier = Modifier.size(4.dp))
        Column {
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Clip,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = song.artist,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
    }
}

@Composable
fun PlayerIconItem(
    modifier: Modifier = Modifier,
    icon: Painter,
    borderStroke: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.onSurface,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        border = borderStroke,
        modifier = modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        contentColor = color,
        color = backgroundColor
    ) {
        Box(
            modifier = modifier.padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = null
            )
        }
    }
}

@Composable
fun MediaPlayerController(
    isSongPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(56.dp)
            .padding(4.dp)
    ) {
        PlayerIconItem(
            icon = if (isSongPlaying)
                painterResource(id = R.drawable.ic_pause_circle)
            else
                painterResource(id = R.drawable.ic_play_circle)
        ) {
            onStart()
        }
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_skip_next),
            modifier = Modifier.clickable {
                onNext()
            },
            contentDescription = null
        )
    }
}

private fun timeStampToDuration(position: Long): String {
    val totalSecond = floor(position / 1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (position < 0)
        "--:--"
    else
        "%d:%02d".format(minutes, remainingSeconds)
}