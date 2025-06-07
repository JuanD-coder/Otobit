package com.juandev.otobit.presentation.screens.playlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaylistsScreen() {
    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Playlists()
    }
}

@Composable
fun Playlists() {
    Text(text = "Play Screen")
}