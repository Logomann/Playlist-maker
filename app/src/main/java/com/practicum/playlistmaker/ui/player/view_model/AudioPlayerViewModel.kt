package com.practicum.playlistmaker.ui.player.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.model.track.TrackInteractor
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.ui.player.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.player.AudioPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PLAYING_TIME_UPDATE_DELAY_MILLIS = 300L

class AudioPlayerViewModel(
    private val apInteractor: AudioPlayerInteractor,
    private val trackInteractor: TrackInteractor
) : ViewModel() {
    private val screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Default)
    private var isPrepared = false
    private var playerState = AudioPlayerState.DEFAULT
    private var timerJob: Job? = null

    fun getScreenStateLiveData(url: String): LiveData<AudioPlayerScreenState> {
        if (!isPrepared) {
            apInteractor.prepare(url)
        }
        apInteractor.execute(url, object : AudioPlayerInteractor.AudioPlayerCallback {
            override fun onComplete() {
                screenStateLiveData.postValue(AudioPlayerScreenState.Completed)
                playerState = AudioPlayerState.COMPLETED
                timerJob?.cancel()
            }

            override fun onPrepared() {
                isPrepared = true
                screenStateLiveData.postValue(AudioPlayerScreenState.Prepared)
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
        playerState = AudioPlayerState.PLAYING
        startTimer()
    }
    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState == AudioPlayerState.PLAYING) {
                delay(PLAYING_TIME_UPDATE_DELAY_MILLIS)
                screenStateLiveData.postValue(AudioPlayerScreenState.Playing(apInteractor.getCurrentPosition()))
            }
        }
    }

    fun pause() {
        timerJob?.cancel()
        apInteractor.pause()
        screenStateLiveData.postValue(AudioPlayerScreenState.Paused)
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

    private fun release() {
        apInteractor.release()
        timerJob?.start()
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

}