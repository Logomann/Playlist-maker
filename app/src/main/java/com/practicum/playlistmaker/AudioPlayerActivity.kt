package com.practicum.playlistmaker

import android.media.MediaPlayer
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
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {
    companion object {
        private const val PLAYING_TIME_UPDATE_DELAY_MILLIS = 300L
    }

    private var playerState = AudioPlayerState.DEFAULT
    private var trackCover: ImageView? = null
    private var playButton: ImageButton? = null
    private var playingTime: TextView? = null
    private var url: String? = ""
    private val handler = Handler(Looper.getMainLooper())
    private val timeOfPlayingRunnable = Runnable { getCurrentPosition() }
    private var mediaPlayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        trackCover = findViewById(R.id.track_cover)
        val bckBtn = findViewById<ImageButton>(R.id.audio_player_arrow)
        bckBtn.setOnClickListener {
            finish()
        }
        playButton = findViewById(R.id.track_play_btn)
        playButton?.isEnabled = false
        playButton?.setOnClickListener {
            playbackControl()
        }
        playingTime = findViewById(R.id.playing_time)
        val trackFromJson = intent.getStringExtra(TRACK)
        val track = Gson().fromJson(trackFromJson, Track::class.java)
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
        val trackUrl = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(trackUrl)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(trackCover!!)

        val trackName = findViewById<TextView>(R.id.song_name)
        trackName.text = track.trackName
        url = track.previewUrl

        val artistName = findViewById<TextView>(R.id.artist_name)
        artistName.text = track.artistName

        val duration = findViewById<TextView>(R.id.track_duration)
        if (!track.trackTimeMillis.isNullOrEmpty()) {
            duration.text = SimpleDateFormat(
                "mm:ss", Locale.getDefault()
            ).format(track.trackTimeMillis.toFloat())
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
        val date = LocalDateTime.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME)
        year.text = date.year.toString()

        val genre = findViewById<TextView>(R.id.track_genre)
        genre.text = track.primaryGenreName

        val country = findViewById<TextView>(R.id.country_name)
        country.text = track.country

        preparePlayer()
    }

    private fun preparePlayer() {
        if (!url.isNullOrEmpty()) {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playButton?.isEnabled = true
                playerState = AudioPlayerState.PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playerState = AudioPlayerState.PREPARED
                playingTime?.text = getString(R.string.start_time_00)
                playButton?.setImageResource(R.drawable.track_play_btn)
            }
        }

    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState = AudioPlayerState.PLAYING
        playButton?.setImageResource(R.drawable.pause_button)
        timeOfPlayingRunnable.run()
    }

    private fun pausePlayer() {
        handler.removeCallbacks(timeOfPlayingRunnable)
        mediaPlayer.pause()
        playerState = AudioPlayerState.PAUSED
        playButton?.setImageResource(R.drawable.track_play_btn)
    }

    private fun playbackControl() {
        when (playerState) {
            AudioPlayerState.PLAYING -> {
                pausePlayer()
            }

            AudioPlayerState.PREPARED, AudioPlayerState.PAUSED -> {
                startPlayer()
            }

            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(timeOfPlayingRunnable)
        mediaPlayer.release()
    }

    @Synchronized
    private fun getCurrentPosition() {
        if (playerState == AudioPlayerState.PLAYING) {
            playingTime?.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(mediaPlayer.currentPosition)
            handler.postDelayed(timeOfPlayingRunnable, PLAYING_TIME_UPDATE_DELAY_MILLIS)
        }
    }
}
