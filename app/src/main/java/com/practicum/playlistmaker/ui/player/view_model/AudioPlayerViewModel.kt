package com.practicum.playlistmaker.ui.player.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.domain.model.track.TrackInteractor
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.ui.player.AudioPlayerState


class AudioPlayerViewModel(
    private val apInteractor: AudioPlayerInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel() {
    private val screenStateLiveData =
        MutableLiveData(AudioPlayerState.DEFAULT)
    private var isPrepared = false
    private var playerState = AudioPlayerState.DEFAULT
    fun getScreenStateLiveData(url: String): LiveData<AudioPlayerState> {
        if (!isPrepared) {
            apInteractor.prepare(url)
        }
        apInteractor.execute(url, object : AudioPlayerInteractor.AudioPlayerCallback {
            override fun onComplete() {
                screenStateLiveData.postValue(AudioPlayerState.COMPLETED)
                playerState = AudioPlayerState.COMPLETED
            }

            override fun onPrepared() {
                isPrepared = true
                screenStateLiveData.postValue(AudioPlayerState.PREPARED)
                playerState = AudioPlayerState.PREPARED
            }

        })
        return screenStateLiveData
    }

    fun getImage(url: String): String {
        return trackInteractor.getLargeImageUrl(url)
    }

    fun getTrack(url: String?): Track {
        val track = trackInteractor.getTrack(url)
        var timeMillis = "00:00"
        if (!track.trackTimeMillis.isNullOrEmpty()) {
            timeMillis = track.trackTimeMillis
        }
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            timeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    private fun play() {
        apInteractor.start()
        screenStateLiveData.postValue(AudioPlayerState.PLAYING)
        playerState = AudioPlayerState.PLAYING
    }

     fun pause() {
        apInteractor.pause()
        screenStateLiveData.postValue(AudioPlayerState.PAUSED)
        playerState = AudioPlayerState.PAUSED
    }

    fun playBack() {
        when (playerState) {
            AudioPlayerState.PLAYING -> {
                pause()
            }

            AudioPlayerState.PAUSED, AudioPlayerState.PREPARED, AudioPlayerState.COMPLETED -> {
                play()
            }

            else -> {}
        }
    }

    fun getCurrentPosition(): String {
        return apInteractor.getCurrentPosition()
    }

    private fun release() {
        apInteractor.release()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AudioPlayerViewModel(
                    Creator.provideAudioPlayerInteractor(),
                    Creator.provideTrackImageInteractor()
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

}