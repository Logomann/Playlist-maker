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
    private val trackImageInteractor: TrackInteractor
) : ViewModel() {
    private val screenStateLiveData =
        MutableLiveData(AudioPlayerState.OnDefault)
    private var isPrepared = false
    fun getScreenStateLiveData(url: String): LiveData<AudioPlayerState> {
        if (!isPrepared) {
            apInteractor.prepare(url)
        }
        apInteractor.execute(url, object : AudioPlayerInteractor.AudioPlayerCallback {
            override fun onComplete() {
                screenStateLiveData.postValue(AudioPlayerState.OnComplete)
            }

            override fun onPrepared() {
                isPrepared = true
                screenStateLiveData.postValue(AudioPlayerState.OnPrepared)
            }

        })
        return screenStateLiveData
    }

    fun setOnPreparedState() {
        screenStateLiveData.postValue(AudioPlayerState.OnPrepared)
    }

    fun getImage(url: String): String {
        return trackImageInteractor.getLargeImageUrl(url)
    }

    fun getTrack(url: String?): Track {
        val track = trackImageInteractor.getTrack(url)
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun play() {
        apInteractor.start()
    }

    fun pause() {
        apInteractor.pause()
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