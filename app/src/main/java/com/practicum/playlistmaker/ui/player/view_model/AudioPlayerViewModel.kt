package com.practicum.playlistmaker.ui.player.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.db.FavoriteTracksInteractor
import com.practicum.playlistmaker.domain.model.track.TrackInteractor
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.domain.player.AudioPlayerInteractor
import com.practicum.playlistmaker.ui.player.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.player.AudioPlayerState
import com.practicum.playlistmaker.ui.player.FavoriteState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val PLAYING_TIME_UPDATE_DELAY_MILLIS = 300L

class AudioPlayerViewModel(
    private val apInteractor: AudioPlayerInteractor,
    private val trackInteractor: TrackInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val screenStateLiveData =
        MutableLiveData<AudioPlayerScreenState>(AudioPlayerScreenState.Default)
    private var isPrepared = false
    private var playerState = AudioPlayerState.DEFAULT
    private var timerJob: Job? = null
    private val favoriteStateLiveData = MutableLiveData<FavoriteState>()
    private lateinit var track: Track

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

    fun isFavorite(): LiveData<FavoriteState> {
        if (track.isFavorite) {
            favoriteStateLiveData.postValue(FavoriteState.Favorite)
        } else {
            favoriteStateLiveData.postValue(FavoriteState.NotFavorite)
        }
        return favoriteStateLiveData
    }

    fun onFavoriteClicked() {
        if (track.isFavorite) {
            viewModelScope.launch(Dispatchers.IO) {
                favoriteTracksInteractor.deleteTrack(track)
            }
            track.isFavorite = false
            favoriteStateLiveData.postValue(FavoriteState.NotFavorite)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                favoriteTracksInteractor.insertTrack(track)
            }
            track.isFavorite = true
            favoriteStateLiveData.postValue(FavoriteState.Favorite)
        }
    }

    fun getTrack(url: String?): Track {
        track = trackInteractor.getTrack(url)
        var timeMillis = "00:00"
        if (!track.trackTimeMillis.isNullOrEmpty()) {
            timeMillis = track.trackTimeMillis!!
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