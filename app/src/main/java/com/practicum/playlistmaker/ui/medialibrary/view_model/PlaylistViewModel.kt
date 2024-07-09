package com.practicum.playlistmaker.ui.medialibrary.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.medialibrary.PlaylistsInteractor
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.medialibrary.PlaylistScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import kotlin.time.Duration.Companion.minutes

class PlaylistViewModel(private val playlistInteractor: PlaylistsInteractor) : ViewModel() {

    private lateinit var playlist: Playlist
    private val listOfTracks = ArrayList<Track>()
    private val screenStateLiveData = MutableLiveData<PlaylistScreenState>()

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .getPlaylist(playlistId)
                .collect { data ->
                    playlist = data
                    playlistInteractor
                        .getAddedTracks(data.plTracksIDs)
                        .collect { tracks ->
                            listOfTracks.clear()
                            listOfTracks.addAll(tracks)
                            screenStateLiveData.postValue(
                                PlaylistScreenState.Content(
                                    data,
                                    calculateTime(tracks),
                                    tracks
                                )
                            )
                        }

                }
        }

    }

    fun render(): LiveData<PlaylistScreenState> {
        return screenStateLiveData
    }

    fun deleteTrackFromPlaylist(track: Track) {
        listOfTracks.remove(track)
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .deleteTrackFromPlaylist(track, playlist)
                .collect { data ->
                    playlist = data
                    screenStateLiveData.postValue(
                        PlaylistScreenState.Content(
                            data,
                            calculateTime(listOfTracks),
                            listOfTracks
                        )
                    )
                }
        }

    }

    fun sharePlaylist(sumOfTracks: String) {
        playlist.plName
        val list =
            StringBuilder().append(playlist.plName).append("\n").append(playlist.plDescription)
                .append("\n").append("${playlist.plTracksIDs.size} $sumOfTracks").append("\n")

        for (i in 0..<listOfTracks.size) {
            list.append("${i + 1}. ${listOfTracks[i].artistName} - ${listOfTracks[i].trackName} (${listOfTracks[i].trackTimeMillis})")
                .append("\n")
        }

        playlistInteractor.sharePlaylist(list.toString())
    }

    private fun calculateTime(tracks: List<Track>): Int {
        var time = 0
        for (track in tracks) {
            val format = SimpleDateFormat("mm:ss", Locale.getDefault())
            val date = format.parse(track.trackTimeMillis.toString())
            val dur = date?.time?.minutes?.inWholeMinutes
            time += SimpleDateFormat(
                "m",
                Locale.getDefault()
            ).format(dur).toInt()

        }
        return time
    }

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .deletePlaylist(playlist)
                .collect {
                    screenStateLiveData.postValue(PlaylistScreenState.Deleted)
                }
        }

    }

}