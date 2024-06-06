package com.practicum.playlistmaker.ui.player.activity

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.ui.player.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerActivity : AppCompatActivity() {
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var binding: ActivityAudioPlayerBinding
    private var playButton: ImageButton? = null
    private var playingTime: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val trackCover = binding.trackCover
        val bckBtn = binding.audioPlayerArrow
        bckBtn.setOnClickListener {
            finish()
        }
        playButton = binding.trackPlayBtn
        playButton?.isEnabled = false
        playButton?.setOnClickListener {
            viewModel.playBack()
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
        duration.text = track.trackTimeMillis


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
                    AudioPlayerScreenState.Prepared -> {
                        playButton?.isEnabled = true
                    }

                    AudioPlayerScreenState.Completed -> {
                        playingTime?.text = getString(R.string.start_time_00)
                        playButton?.setImageResource(R.drawable.track_play_btn)
                    }

                    is AudioPlayerScreenState.Playing -> {
                        startPlayer()
                        playingTime?.text = screenState.progress
                    }

                    AudioPlayerScreenState.Paused -> {
                        playButton?.isEnabled = true
                        pausePlayer()
                    }

                    else -> { }
                }
            }
        }

    }


    private fun startPlayer() {
        playButton?.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        playButton?.setImageResource(R.drawable.track_play_btn)
    }


    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

}

