package com.practicum.playlistmaker


import com.practicum.playlistmaker.data.audioplayer.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.track.TrackRepositoryImpl
import com.practicum.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import com.practicum.playlistmaker.domain.api.track.TrackInteractor
import com.practicum.playlistmaker.domain.api.track.TrackRepository
import com.practicum.playlistmaker.domain.impl.audioplayer.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.domain.impl.track.TrackInteractorImpl

object Creator {
    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl()
    }
    fun provideTrackImageInteractor():TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }
}