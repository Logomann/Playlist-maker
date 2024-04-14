package com.practicum.playlistmaker.util

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


const val PREFERENCES = "preferences"
const val THEME_KEY = "key_for_theme"
const val TRACK_KEY = "TRACK_KEY"

class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var application: Application
    override fun onCreate() {
        super.onCreate()
        application = this
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(THEME_KEY, darkTheme)
        switchTheme(darkTheme)
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
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