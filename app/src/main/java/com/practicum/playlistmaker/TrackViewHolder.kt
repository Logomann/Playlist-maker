package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameOfSong: TextView = itemView.findViewById(R.id.name_of_song)
    private val nameOfArtist: TextView = itemView.findViewById(R.id.name_of_artist)
    private val trackDuration: TextView = itemView.findViewById(R.id.playing_time)
    private val songImage: ImageView = itemView.findViewById(R.id.song_image)
    fun bind(model: Track) {
        nameOfSong.text = model.trackName
        nameOfArtist.text = model.artistName
        if (!model.trackTimeMillis.isNullOrEmpty()) {
            trackDuration.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(model.trackTimeMillis.toFloat())
        } else {
            trackDuration.text = itemView.context.getString(R.string.start_time_00)
        }

        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(songImage)
    }
}