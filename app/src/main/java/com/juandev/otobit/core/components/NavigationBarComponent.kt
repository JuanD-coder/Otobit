package com.juandev.otobit.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.juandev.otobit.core.navigation.NavItemList
import com.juandev.otobit.core.navigation.RouteDestination
import com.juandev.otobit.domain.model.NavItem

@Composable
fun NavigationBarComponent(
    navItemList: List<NavItem>,
    currentRoute: RouteDestination?,
    onItemSelected: (RouteDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        navItemList.forEach { item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                onClick = { onItemSelected(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) }
            )
        }
    }
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