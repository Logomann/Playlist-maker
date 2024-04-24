package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.medialibrary.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.ui.medialibrary.view_model.PlaylistViewModel
import com.practicum.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        FavoriteTracksViewModel(get())
    }
    viewModel {
        PlaylistViewModel(get())
    }
}