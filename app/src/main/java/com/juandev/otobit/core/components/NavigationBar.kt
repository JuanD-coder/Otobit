package com.juandev.otobit.core.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.juandev.otobit.domain.model.NavItem

@Composable
fun NavigationBar(
    navItemList: List<NavItem>,
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        navItemList.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemSelected(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(text = item.label) }
            )
        }
    }
}