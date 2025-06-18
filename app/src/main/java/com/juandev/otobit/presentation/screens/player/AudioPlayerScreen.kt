package com.juandev.otobit.presentation.screens.player

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AudioPlayerScreen(viewModel: PlayerViewModel = hiltViewModel(), songUris: List<Uri>) {
    Column {
        LazyColumn {

        }
    }
}


