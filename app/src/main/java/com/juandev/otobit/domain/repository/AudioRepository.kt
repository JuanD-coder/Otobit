package com.juandev.otobit.domain.repository

import com.juandev.otobit.domain.model.Audio

interface AudioRepository {

    suspend fun getAudioData(): List<Audio>

}