package com.practicum.playlistmaker.data.settings.impl

import androidx.activity.ComponentActivity
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.PREFERENCES
import com.practicum.playlistmaker.util.THEME_KEY
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl : SettingsRepository {
    private val application = App.getApplication()
    private val sharedPreferences =
        application.getSharedPreferences(PREFERENCES, ComponentActivity.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        val isDark = sharedPreferences.getBoolean(THEME_KEY, false)
        if (isDark) return ThemeSettings.DARK
        return ThemeSettings.LIGHT
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(THEME_KEY, settings.isDark).apply()
        (application as App).switchTheme(settings.isDark)
    }
}