package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        if (settings == ThemeSettings.DARK) {
            repository.updateThemeSetting(ThemeSettings.LIGHT)
        } else {
            repository.updateThemeSetting(ThemeSettings.DARK)
        }

    }
}