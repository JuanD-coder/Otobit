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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.juandev.otobit.core.components.NavigationBarComponent
import com.juandev.otobit.core.navigation.NavItemList
import com.juandev.otobit.core.navigation.RouteDestination

@Composable
fun HomeScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteString = navBackStackEntry?.destination?.route

    val currentNavItemRoute: RouteDestination? = NavItemList.navItemList.find { routeObject ->
        routeObject.route::class.qualifiedName == currentRouteString
                || routeObject.toString() == currentRouteString
    }?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(navController, currentNavItemRoute)
        }
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

@Composable
fun NavigationBar(navController: NavHostController, currentNavItemRoute: RouteDestination?) {
    if (NavItemList.navItemList.isNotEmpty()) {
        NavigationBarComponent(
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
