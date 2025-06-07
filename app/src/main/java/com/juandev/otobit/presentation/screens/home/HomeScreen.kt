package com.juandev.otobit.presentation.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Box(Modifier.fillMaxSize().padding(16.dp)) {
        Home()
    }
}

@Composable
fun Home() {
    HeaderActions()
    //SuggestMusic()
    //AddedMusic()
}

@Composable
fun HeaderActions() {
    Text(text = "Home Screen")
}

@Composable
fun AddedMusic() {
    TODO("Not yet implemented")
}

@Composable
fun SuggestMusic() {
    TODO("Not yet implemented")
}
