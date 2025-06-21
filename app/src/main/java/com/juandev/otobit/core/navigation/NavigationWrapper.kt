package com.juandev.otobit.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juandev.otobit.presentation.screens.allsongs.AllSongsScreen
import com.juandev.otobit.presentation.screens.artists.ArtistsScreen
import com.juandev.otobit.presentation.screens.home.HomeScreen
import com.juandev.otobit.presentation.screens.playlist.PlaylistsScreen
import com.juandev.otobit.presentation.screens.splash.SplashScreen

@Composable
fun NavigationWrapper(
    navController: NavHostController,
    modifier: Modifier,
    isServiceRunning: Unit
) {

    NavHost(
        navController = navController,
        startDestination = Splash,
        modifier = modifier
    ) {
        composable<Splash> {
            SplashScreen {
                navController.navigate(Home) {
                    popUpTo(Splash) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable<Home> { HomeScreen() }
        composable<AllSongs> {
            AllSongsScreen(isServiceRunning = isServiceRunning)
        }
        composable<Artists> { ArtistsScreen() }
        composable<PlayList> { PlaylistsScreen() }
    }
}
