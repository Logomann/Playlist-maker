package com.practicum.playlistmaker.domain.player


interface AudioPlayerInteractor {
    fun execute(url: String, playerCallback: AudioPlayerCallback)

    interface AudioPlayerCallback {
        fun onComplete()
        fun onPrepared()
    }
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()

    fun getCurrentPosition(): String


}