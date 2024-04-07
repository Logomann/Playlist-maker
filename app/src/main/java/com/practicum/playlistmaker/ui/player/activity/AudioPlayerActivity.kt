package com.practicum.playlistmaker.ui.player.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.ui.player.AudioPlayerState
import com.practicum.playlistmaker.ui.player.view_model.AudioPlayerViewModel


class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding

    companion object {
        private const val PLAYING_TIME_UPDATE_DELAY_MILLIS = 300L
    }


    private var playButton: ImageButton? = null
    private var playingTime: TextView? = null
    private val handler = Handler(Looper.getMainLooper())
    private val timeOfPlayingRunnable = Runnable { getCurrentPosition() }
    private var playerState = PlayerState.DEFAULT
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

        val trackCover = binding.trackCover
        val bckBtn = binding.audioPlayerArrow
        bckBtn.setOnClickListener {
            finish()
        }
        playButton = binding.trackPlayBtn
        playButton?.isEnabled = false
        playButton?.setOnClickListener {
            playBack()
        }

        playingTime = binding.playingTime
        val url: String?
        val track = viewModel.getTrack(intent.getStringExtra(TRACK_KEY))
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
        val trackUrl = viewModel.getImage(track.artworkUrl100)

        Glide.with(this)
            .load(trackUrl)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(trackCover)

        val trackName = binding.songName
        trackName.text = track.trackName
        url = track.previewUrl


        val artistName = binding.artistName
        artistName.text = track.artistName

        val duration = binding.trackDuration
        if (!track.trackTimeMillis.isNullOrEmpty()) {
            duration.text = track.trackTimeMillis
        } else {
            duration.text = getString(R.string.start_time_00)
        }

        playingTime!!.text = viewModel.getCurrentPosition()

        val albumGroup = binding.albumGroup

        if (track.collectionName.isNotEmpty()) {
            albumGroup.isVisible = true
            val album = binding.albumName
            album.text = track.collectionName
        } else {
            albumGroup.isVisible = false
        }

        val year = binding.trackYear
        year.text = track.releaseDate

        val genre = binding.trackGenre
        genre.text = track.primaryGenreName

        val country = binding.countryName
        country.text = track.country
        if (!url.isNullOrEmpty()) {
            viewModel.getScreenStateLiveData(url).observe(this) { screenState ->
                when (screenState) {
                    AudioPlayerState.OnPrepared -> {
                        playButton?.isEnabled = true
                        playerState = PlayerState.PREPARED
                    }

                    AudioPlayerState.OnComplete -> {
                        playingTime?.text = getString(R.string.start_time_00)
                        playButton?.setImageResource(R.drawable.track_play_btn)
                        playerState = PlayerState.PREPARED
                        viewModel.setOnPreparedState()
                    }

                    else -> {}
                }
            }
        }

    }


    private fun playBack() {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }

            PlayerState.PAUSED, PlayerState.PREPARED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    @Synchronized
    private fun getCurrentPosition() {
        if (playerState == PlayerState.PLAYING) {
            playingTime?.text = viewModel.getCurrentPosition()
            handler.postDelayed(timeOfPlayingRunnable, PLAYING_TIME_UPDATE_DELAY_MILLIS)
        }
    }

    private fun startPlayer() {
        viewModel.play()
        playerState = PlayerState.PLAYING
        playButton?.setImageResource(R.drawable.pause_button)
        timeOfPlayingRunnable.run()
    }

    private fun pausePlayer() {
        handler.removeCallbacks(timeOfPlayingRunnable)
        viewModel.pause()
        playerState = PlayerState.PAUSED
        playButton?.setImageResource(R.drawable.track_play_btn)
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeOfPlayingRunnable)
    }

}

enum class PlayerState {
    DEFAULT,
    PREPARED,
    PLAYING,
    PAUSED
}
