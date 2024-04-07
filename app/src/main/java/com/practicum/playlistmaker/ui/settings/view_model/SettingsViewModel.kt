package com.practicum.playlistmaker.ui.settings.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor


class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }

    fun getTheme(): Boolean {
        return settingsInteractor.getThemeSettings().isDark
    }

    fun switchTheme() {
        settingsInteractor.updateThemeSetting(settingsInteractor.getThemeSettings())
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    Creator.provideSharingInteractor(),
                    Creator.provideSettingsInteractor()
                )
            }
        }
    }
}