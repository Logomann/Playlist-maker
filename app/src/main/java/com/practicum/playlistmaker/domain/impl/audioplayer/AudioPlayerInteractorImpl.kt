package com.practicum.playlistmaker.domain.impl.audioplayer


import com.practicum.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.api.audioplayer.AudioPlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerInteractorImpl(private val repository: AudioPlayerRepository) :
    AudioPlayerInteractor {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun execute(url: String?, playerCallback: AudioPlayerInteractor.AudioPlayerCallback) {
        if (!url.isNullOrEmpty()) {
            repository.preparePlayer(url, object : AudioPlayerRepository.AudioPlayerCallback {
                override fun onComplete() {
                    playerCallback.onComplete()
                }

                override fun onPrepared() {
                    playerCallback.onPrepared()
                }

            })

        }
    }
    override fun start() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun release() {
        repository.release()
    }
    override fun getCurrentPosition(): String {
        return dateFormat.format(repository.getCurrentPosition())
    }

}