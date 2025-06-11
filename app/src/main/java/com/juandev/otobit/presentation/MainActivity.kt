package com.juandev.otobit.presentation

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.juandev.otobit.core.components.NavigationBar
import com.juandev.otobit.core.navigation.NavItemList
import com.juandev.otobit.core.navigation.NavigationWrapper
import com.juandev.otobit.presentation.ui.theme.OtobitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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

@Preview(showBackground = true)
@Composable
fun BottomNavScreen() {
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
            modifier = Modifier.padding(innerPadding)
        )
    }
}