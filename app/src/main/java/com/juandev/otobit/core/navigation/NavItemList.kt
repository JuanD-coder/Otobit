package com.juandev.otobit.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import com.juandev.otobit.domain.model.NavItem

object NavItemList {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home, Routes.HOME),
        NavItem("Songs", Icons.Default.PlayArrow, Routes.ALL_SONGS),
        NavItem("Artists", Icons.Default.Face, Routes.ARTISTS),
        NavItem("PlayList", Icons.Default.List, Routes.PLAYLIST)
    )
}

//data class NavItem(val label: String, val icon: ImageVector, val route: String)