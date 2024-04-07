package com.practicum.playlistmaker.domain.player


interface AudioPlayerRepository {

    fun getPlayerState(url: String, playerCallback: AudioPlayerCallback)

    interface AudioPlayerCallback {
        fun onComplete()
        fun onPrepared()
    }
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition(): Int


}