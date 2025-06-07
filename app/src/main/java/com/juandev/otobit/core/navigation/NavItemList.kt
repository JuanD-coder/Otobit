package com.juandev.otobit.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import com.juandev.otobit.domain.model.NavItem

object NavItemList {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home, Home),
        NavItem("Songs", Icons.Default.PlayArrow, AllSongs),
        NavItem("Artists", Icons.Default.Face, Artists),
        NavItem("PlayList", Icons.Default.List, PlayList)
    )
}