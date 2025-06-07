package com.juandev.otobit.core.navigation

import kotlinx.serialization.Serializable

interface RouteDestination

@Serializable
object Home: RouteDestination

@Serializable
object Artists: RouteDestination

@Serializable
object AllSongs: RouteDestination

@Serializable
object PlayList: RouteDestination

@Serializable
object Permissions: RouteDestination

@Serializable
object PlayerNow

@Serializable
object Settings
