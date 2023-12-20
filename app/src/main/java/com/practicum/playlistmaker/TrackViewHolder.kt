package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameOfSong: TextView = itemView.findViewById(R.id.name_of_song)
    private val nameOfArtist: TextView = itemView.findViewById(R.id.name_of_artist)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)
    private val songImage: ImageView = itemView.findViewById(R.id.song_image)
    fun bind(model: Track) {
        nameOfSong.text = model.trackName
        nameOfArtist.text = model.artistName
        trackDuration.text = model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(songImage)

    }
}