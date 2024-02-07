package com.practicum.playlistmaker

import android.os.Bundle
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
    private lateinit var trackCover: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        trackCover = findViewById(R.id.track_cover)
        val bckBtn = findViewById<ImageButton>(R.id.audio_player_arrow)
        bckBtn.setOnClickListener {
            this.finish()
        }
        val trackFromJson = intent.getStringExtra("track")
        val track = Gson().fromJson(trackFromJson, Track::class.java)
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
        val trackUrl = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(trackUrl)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(trackCover)
        val trackName = findViewById<TextView>(R.id.song_name)
        trackName.text = track.trackName

        val artistName = findViewById<TextView>(R.id.artist_name)
        artistName.text = track.artistName

        val duration = findViewById<TextView>(R.id.track_duration)
        duration.text = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        ).format(track.trackTimeMillis.toFloat())

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

    }
}