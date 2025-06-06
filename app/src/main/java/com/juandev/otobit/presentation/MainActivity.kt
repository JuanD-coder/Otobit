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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.juandev.otobit.core.components.NavigationBarComponent
import com.juandev.otobit.core.navigation.NavItemList
import com.juandev.otobit.core.navigation.NavigationWrapper
import com.juandev.otobit.core.navigation.RouteDestination
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

    val currentNavItemRoute: RouteDestination? = NavItemList.navItemList.find { routeObject ->
        routeObject.route::class.qualifiedName == currentRouteString || routeObject.toString() == currentRouteString
    }?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(navController, currentNavItemRoute)
        }
    ) { innerPadding ->
        NavigationWrapper(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun NavigationBar(navController: NavHostController, currentNavItemRoute: RouteDestination?) {
    if (NavItemList.navItemList.isNotEmpty()) {
        return NavigationBarComponent(
            navItemList = NavItemList.navItemList,
            currentRoute = currentNavItemRoute,
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
}