package com.juandev.otobit.data.repository

import com.juandev.otobit.data.local.ContentResolverHelper
import com.juandev.otobit.domain.model.Audio
import com.juandev.otobit.domain.repository.AudioRepository
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRepositoryImpl @Inject constructor(
    private val contentResolverHelper: ContentResolverHelper
): AudioRepository {

    override suspend fun getAudioData(): List<Audio> = withContext(Dispatchers.IO) {
        contentResolverHelper.getAudioData()
    }

}