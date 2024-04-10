package com.practicum.playlistmaker.data.player


import com.practicum.playlistmaker.domain.player.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var mediaPlayer = AudioPlayer().getMediaPlayer()
    override fun getPlayerState(
        url: String,
        playerCallback: AudioPlayerRepository.AudioPlayerCallback
    ) {
        mediaPlayer.setOnPreparedListener {
            playerCallback.onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            it.seekTo(0)
            playerCallback.onComplete()
        }
    }

    override fun prepare(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }


    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}