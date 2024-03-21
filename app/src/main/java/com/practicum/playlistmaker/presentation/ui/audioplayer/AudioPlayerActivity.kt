package com.practicum.playlistmaker.presentation.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.data.audioplayer.AudioPlayerState
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.TRACK
import com.practicum.playlistmaker.domain.api.audioplayer.AudioPlayerInteractor


class AudioPlayerActivity : AppCompatActivity() {
    companion object {
        private const val PLAYING_TIME_UPDATE_DELAY_MILLIS = 300L
    }


    private var playButton: ImageButton? = null
    private var playingTime: TextView? = null
    private val handler = Handler(Looper.getMainLooper())
    private val timeOfPlayingRunnable = Runnable { getCurrentPosition() }
    private val audioPlayerInteractor = Creator.provideAudioPlayerInteractor()
    private val trackInteractor = Creator.provideTrackImageInteractor()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val trackCover: ImageView = findViewById(R.id.track_cover)
        val bckBtn = findViewById<ImageButton>(R.id.audio_player_arrow)
        bckBtn.setOnClickListener {
            finish()
        }

        playButton = findViewById(R.id.track_play_btn)
        playButton?.isEnabled = false
        playButton?.setOnClickListener {
            playBack()
        }
        playingTime = findViewById(R.id.playing_time)
        val url: String?
        val track = trackInteractor.getTrack(intent.getStringExtra(TRACK))
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
        val trackUrl = trackInteractor.getLargeImageUrl(track.artworkUrl100)

        Glide.with(this)
            .load(trackUrl)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(trackCover)

        val trackName = findViewById<TextView>(R.id.song_name)
        trackName.text = track.trackName
        url = track.previewUrl

        val artistName = findViewById<TextView>(R.id.artist_name)
        artistName.text = track.artistName

        val duration = findViewById<TextView>(R.id.track_duration)
        if (!track.trackTimeMillis.isNullOrEmpty()) {
            duration.text = track.trackTimeMillis
        } else {
            duration.text = getString(R.string.start_time_00)
        }


        val albumGroup = findViewById<Group>(R.id.albumGroup)

        if (track.collectionName.isNotEmpty()) {
            albumGroup.isVisible = true
            val album = findViewById<TextView>(R.id.album_name)
            album.text = track.collectionName
        } else {
            albumGroup.isVisible = false
        }

        val year = findViewById<TextView>(R.id.track_year)
        year.text = track.releaseDate

        val genre = findViewById<TextView>(R.id.track_genre)
        genre.text = track.primaryGenreName

        val country = findViewById<TextView>(R.id.country_name)
        country.text = track.country

        audioPlayerInteractor.execute(url, object : AudioPlayerInteractor.AudioPlayerCallback {
            override fun onComplete() {
                playingTime?.text = getString(R.string.start_time_00)
                playButton?.setImageResource(R.drawable.track_play_btn)
            }

            override fun onPrepared() {
                playButton?.isEnabled = true
            }
        })
    }


    private fun playBack() {
        when (audioPlayerInteractor.getState()) {
            AudioPlayerState.PLAYING -> {
                pausePlayer()
            }

            AudioPlayerState.PAUSED, AudioPlayerState.PREPARED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    @Synchronized
    private fun getCurrentPosition() {
        if (audioPlayerInteractor.getState() == AudioPlayerState.PLAYING) {
            playingTime?.text = audioPlayerInteractor.getCurrentPosition()
            handler.postDelayed(timeOfPlayingRunnable, PLAYING_TIME_UPDATE_DELAY_MILLIS)
        }
    }

    private fun startPlayer() {
        audioPlayerInteractor.start()
        playButton?.setImageResource(R.drawable.pause_button)
        timeOfPlayingRunnable.run()
    }

    private fun pausePlayer() {
        handler.removeCallbacks(timeOfPlayingRunnable)
        audioPlayerInteractor.pause()
        playButton?.setImageResource(R.drawable.track_play_btn)
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeOfPlayingRunnable)
        audioPlayerInteractor.release()
    }

}
