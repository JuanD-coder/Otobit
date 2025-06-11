package com.juandev.otobit.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            Column {
                HeaderActions()
                //SuggestMusic()
                //AddedMusic()
            }
        }

    }
}

@Composable
fun HeaderActions() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(text = "Home Screen")
    }
}

@Composable
fun AddedMusic() {
    TODO("Not yet implemented")
}

@Composable
fun SuggestMusic() {
    TODO("Not yet implemented")
}
