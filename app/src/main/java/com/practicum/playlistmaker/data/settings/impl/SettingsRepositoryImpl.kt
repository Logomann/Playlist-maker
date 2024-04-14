package com.practicum.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.THEME_KEY
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val app: App
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val isDark = sharedPreferences.getBoolean(THEME_KEY, false)
        if (isDark) return ThemeSettings.DARK
        return ThemeSettings.LIGHT
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit {
            putBoolean(THEME_KEY, settings.isDark)
        }
        app.switchTheme(settings.isDark)
    }
}