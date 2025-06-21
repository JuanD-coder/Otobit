package com.juandev.otobit.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import com.juandev.otobit.core.player.notification.JetAudioNotificationManager
import com.juandev.otobit.core.player.service.JetAudioServiceHandler
import com.juandev.otobit.data.local.ContentResolverHelper
import com.juandev.otobit.data.repository.AppSettingsRepositoryImpl
import com.juandev.otobit.data.repository.SongRepositoryImpl
import com.juandev.otobit.domain.repository.AppSettingsRepository
import com.juandev.otobit.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {

    /**
     * Proporciona una instancia única de [AudioAttributes].
     * Estos atributos definen cómo se reproducirá el audio,
     * especificando el tipo de contenido (música) y el uso (media).
     *
     * @return Una instancia de [AudioAttributes].
     */
    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    /**
     * Proporciona una instancia única de [ExoPlayer].
     * ExoPlayer es el reproductor de medios utilizado en la aplicación.
     *
     * @return Una instancia de [ExoPlayer].
     */
    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()

    /**
     * Proporciona una instancia única de [MediaSession].
     * MediaSession se utiliza para integrar la reproducción de medios con el sistema Android,
     * permitiendo el control desde notificaciones, pantalla de bloqueo, etc.
     *
     * @param context El contexto de la aplicación.
     * @param player La instancia de [ExoPlayer] que gestionará la reproducción.
     * @return Una instancia de [MediaSession].
     */
    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): MediaSession = MediaSession.Builder(context, player).build()

    /**
     * Proporciona una instancia única de [JetAudioNotificationManager].
     * Esta clase gestiona las notificaciones relacionadas con la reproducción de audio.
     *
     * @param context El contexto de la aplicación.
     * @param player La instancia de [ExoPlayer] para la cual se mostrarán las notificaciones.
     * @return Una instancia de [JetAudioNotificationManager].
     */
    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ): JetAudioNotificationManager = JetAudioNotificationManager(
        context = context,
        exoPlayer = player
    )

    /**
     * Proporciona una instancia única de [JetAudioServiceHandler].
     * Esta clase actúa como un manejador para el servicio de reproducción de audio,
     * interactuando con [ExoPlayer] para controlar la reproducción.
     *
     * @param exoPlayer La instancia de [ExoPlayer] que será gestionada por el servicio.
     * @return Una instancia de [JetAudioServiceHandler].
     */
    @Provides
    @Singleton
    fun provideServiceHandler(exoPlayer: ExoPlayer): JetAudioServiceHandler =
        JetAudioServiceHandler(exoPlayer)

}