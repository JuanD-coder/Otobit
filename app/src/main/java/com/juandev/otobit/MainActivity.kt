package com.juandev.otobit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.juandev.otobit.core.components.NavigationBar
import com.juandev.otobit.core.navigation.NavItemList
import com.juandev.otobit.core.navigation.NavigationWrapper
import com.juandev.otobit.presentation.allsongs.AllSongsScreen
import com.juandev.otobit.presentation.artists.ArtistsScreen
import com.juandev.otobit.presentation.home.HomeScreen
import com.juandev.otobit.presentation.playlist.PlaylistsScreen
import com.juandev.otobit.ui.theme.OtobitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OtobitTheme {
                BottomNavScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun BottomNavScreen() {
    val navController = rememberNavController()

    Scaffold (
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route

            NavigationBar(
                navItemList = NavItemList.navItemList,
                currentRoute = currentDestination,
                onItemSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) {
        NavigationWrapper(navController = navController)
    }
}