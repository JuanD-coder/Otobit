package com.juandev.otobit.data.repository

import com.juandev.otobit.data.local.ContentResolverHelper
import com.juandev.otobit.domain.model.SongData
import com.juandev.otobit.domain.repository.SongRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRepositoryImpl @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper
): SongRepository {

    override suspend fun getSongData(): List<SongData> = withContext(Dispatchers.IO) {
        contentResolverHelper.getAudioData()
    }

}