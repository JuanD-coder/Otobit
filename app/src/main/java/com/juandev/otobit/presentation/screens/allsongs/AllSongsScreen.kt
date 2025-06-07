package com.juandev.otobit.presentation.screens.allsongs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AllSongsScreen() {
    Box(Modifier.fillMaxSize().padding(16.dp)) {
        AllSongs()
    }
}

@Composable
fun AllSongs() {
    Text(text = "Songs Screen")
}