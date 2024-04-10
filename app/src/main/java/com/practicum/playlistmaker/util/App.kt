package com.practicum.playlistmaker.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatDelegate


const val PREFERENCES = "preferences"
const val THEME_KEY = "key_for_theme"
const val TRACK_KEY = "TRACK_KEY"

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var application: Application
        fun getApplication(): Application {
            return application
        }
        private lateinit var sharedPrefs: SharedPreferences
        fun getSharedPreferences() : SharedPreferences {
            sharedPrefs = application.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
            return sharedPrefs
        }
        private lateinit var connectivityManager: ConnectivityManager
        fun getConnectivityManager(): ConnectivityManager {
            connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager
        }
    }

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        application = this
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