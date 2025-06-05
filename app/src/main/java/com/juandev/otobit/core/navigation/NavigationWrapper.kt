package com.juandev.otobit.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juandev.otobit.presentation.allsongs.AllSongsScreen
import com.juandev.otobit.presentation.artists.ArtistsScreen
import com.juandev.otobit.presentation.home.HomeScreen
import com.juandev.otobit.presentation.playlist.PlaylistsScreen

@Composable
fun NavigationWrapper(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { HomeScreen() }
        composable(Routes.ALL_SONGS) { AllSongsScreen() }
        composable(Routes.ARTISTS) { ArtistsScreen() }
        composable(Routes.PLAYLIST) { PlaylistsScreen() }
    }
}