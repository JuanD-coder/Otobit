package com.juandev.otobit.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juandev.otobit.presentation.screens.allsongs.AllSongsScreen
import com.juandev.otobit.presentation.screens.artists.ArtistsScreen
import com.juandev.otobit.presentation.screens.home.HomeScreen
import com.juandev.otobit.presentation.screens.permissions.PermissionsScreen
import com.juandev.otobit.presentation.screens.playlist.PlaylistsScreen

@Composable
fun NavigationWrapper(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = Permissions,
        modifier = modifier
    ) {
        composable<Permissions> {
            PermissionsScreen(
                onContinue = {
                    navController.navigate(Home) { // Asume que HomeRoute es tu pantalla principal
                        popUpTo(Permissions) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<Home> { HomeScreen() }
        composable<AllSongs> { AllSongsScreen() }
        composable<Artists> { ArtistsScreen() }
        composable<PlayList> { PlaylistsScreen() }
    }
}