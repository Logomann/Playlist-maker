package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.medialibrary.impl.FavoriteTracksRepositoryImpl
import com.practicum.playlistmaker.data.medialibrary.impl.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.data.player.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.medialibrary.FavoriteTracksRepository
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsRepository
import com.practicum.playlistmaker.domain.model.track.TrackRepository
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository
import com.practicum.playlistmaker.domain.search.SearchHistoryRepository
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.sharing.ExternalNavigator
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchRepository> {
        SearchRepositoryImpl(get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }
    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }
    single<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(get(),get())
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(),get())
    }
    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(),get())
    }


}