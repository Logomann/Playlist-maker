package com.practicum.playlistmaker.ui.search


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.domain.model.track.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Track) {
        binding.nameOfSong.text = model.artistName
        binding.nameOfArtist.text = model.artistName
        if (!model.trackTimeMillis.isNullOrEmpty()) {
            binding.playingTime.text = SimpleDateFormat(
                "mm:ss",
                Locale.getDefault()
            ).format(model.trackTimeMillis.toFloat())
        } else {
            binding.playingTime.text = itemView.context.getString(R.string.start_time_00)
        }

        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)

        Glide.with(binding.root)
            .load(model.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(binding.songImage)
    }
}