package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.THEME_KEY
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.model.ThemeSettings
import com.practicum.playlistmaker.util.PREFERENCES

class SettingsRepositoryImpl(
    private val context: Context,
    private val app: App
) : SettingsRepository {
    private lateinit var sharedPreferences: SharedPreferences
    override fun getThemeSettings(): ThemeSettings {
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Application.MODE_PRIVATE)
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