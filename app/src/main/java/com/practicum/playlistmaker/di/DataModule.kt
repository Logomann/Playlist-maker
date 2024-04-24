package com.practicum.playlistmaker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.player.AudioPlayer
import com.practicum.playlistmaker.data.search.network.ITunesApi
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.domain.sharing.ExternalNavigator
import com.practicum.playlistmaker.util.App
import com.practicum.playlistmaker.util.PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(
            get(), androidContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
        )
    }
    single {
        AudioPlayer().getMediaPlayer()
    }


    single<App> {
        androidContext() as App
    }
    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
    single<SharedPreferences>{
        androidContext().getSharedPreferences(PREFERENCES, Application.MODE_PRIVATE)
    }

}