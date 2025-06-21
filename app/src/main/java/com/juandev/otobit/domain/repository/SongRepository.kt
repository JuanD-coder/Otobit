package com.juandev.otobit.domain.repository

import com.juandev.otobit.domain.model.SongData

interface SongRepository {

    suspend fun getSongData(): List<SongData>

}