package com.juandev.otobit.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.juandev.otobit.core.navigation.RouteDestination

data class NavItem(
    val label: String,
    val icon: ImageVector,
    val route: RouteDestination
)