package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate


const val PREFERENCES = "preferences"
const val THEME_KEY = "key_for_theme"
const val TRACK_KEY = "TRACK_KEY"

class App : Application() {
    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_KEY, darkTheme)
        switchTheme(darkTheme)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        sharedPrefs.edit().putBoolean(THEME_KEY, darkThemeEnabled).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}