package com.practicum.playlistmaker.data.audioplayer

import com.practicum.playlistmaker.domain.api.audioplayer.AudioPlayerRepository

class AudioPlayerRepositoryImpl : AudioPlayerRepository {

    private var playerState = AudioPlayerState.DEFAULT
    private var mediaPlayer = AudioPlayer().getMediaPlayer()

    override fun preparePlayer(
        url: String,
        playerCallback: AudioPlayerRepository.AudioPlayerCallback
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = AudioPlayerState.PREPARED
            playerCallback.onPrepared()
        }
        mediaPlayer.setOnCompletionListener {
            playerState = AudioPlayerState.PREPARED
            playerCallback.onComplete()
        }
    }

    override fun start() {
        mediaPlayer.start()
        playerState = AudioPlayerState.PLAYING
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = AudioPlayerState.PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}