package com.juandev.otobit.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.juandev.otobit.core.components.NavigationBar
import com.juandev.otobit.core.navigation.NavItemList
import com.juandev.otobit.core.navigation.NavigationWrapper
import com.juandev.otobit.core.player.service.JetAudioService
import com.juandev.otobit.presentation.ui.theme.OtobitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OtobitTheme {
                BottomNavScreen(startService())
            }
        }
    }

    private fun startService() {
        if (!isServiceRunning) {
            val intent = Intent(this, JetAudioService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            isServiceRunning = true
        }
    }
}

@Composable
fun BottomNavScreen(isServiceRunning: Unit) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteString = navBackStackEntry?.destination?.route

    val currentNavItem = NavItemList.navItemList.find { navItem ->
        val itemRouteString = navItem.route.toString()
        itemRouteString == currentRouteString || navItem.route::class.qualifiedName == currentRouteString
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            currentNavItem?.let { activeNavItem ->
                NavigationBar(
                    navController = navController,
                    currentNavItemRoute = activeNavItem.route
                )
            }
        }
    ) { innerPadding ->
        NavigationWrapper(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            isServiceRunning = isServiceRunning
        )
    }
}