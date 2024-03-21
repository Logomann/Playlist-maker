package com.practicum.playlistmaker.domain.api.audioplayer



interface AudioPlayerInteractor {
    fun execute(url : String?, playerCallback: AudioPlayerCallback)

    interface AudioPlayerCallback {
        fun onComplete()
        fun onPrepared()
    }


    fun start()
    fun pause()
    fun release()

    fun getState() : Any

    fun getCurrentPosition() : String


}