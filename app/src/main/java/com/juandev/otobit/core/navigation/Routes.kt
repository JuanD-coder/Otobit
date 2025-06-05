package com.juandev.otobit.core.navigation

object Routes {
    // Pantallas principales
    const val HOME = "home"
    const val ARTISTS = "artists"
    const val ALL_SONGS = "allSongs"
    const val PLAYLIST = "playList"
    const val PLAYER = "player"
    const val SETTINGS = "settings"

    // Pantalla con argumento (trackId)
    const val NOW_PLAYING = "now_playing/{trackId}"

    // Función para navegar con trackId dinámico
    fun nowPlaying(trackId: Long) = "now_playing/$trackId"
}