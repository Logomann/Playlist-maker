package com.practicum.playlistmaker.domain.api.audioplayer



interface AudioPlayerRepository {

   fun preparePlayer(url: String, playerCallback: AudioPlayerCallback)

 interface AudioPlayerCallback {
  fun onComplete()
  fun onPrepared()
 }

    fun start()
    fun pause()
    fun release()
    fun getCurrentPosition() : Int


}