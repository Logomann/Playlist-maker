package com.practicum.playlistmaker.data.player


import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.AudioPlayerRepository

class AudioPlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : AudioPlayerRepository {
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
        mediaPlayer.reset()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}