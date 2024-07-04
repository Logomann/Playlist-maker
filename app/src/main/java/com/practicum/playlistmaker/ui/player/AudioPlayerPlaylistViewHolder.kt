package com.practicum.playlistmaker.ui.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistViewAudioplayerBinding
import com.practicum.playlistmaker.domain.model.Playlist
import java.util.Locale

class AudioPlayerPlaylistViewHolder(private val binding: PlaylistViewAudioplayerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)
        Glide.with(binding.root)
            .load(playlist.plCover)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(binding.playlistImage)


        binding.nameOfPlaylist.text = playlist.plName
        val size = playlist.plTracksIDs.size
        val track = when (size) {
            1 -> binding.root.context.getString(R.string.track)
            in 2..4 -> binding.root.context.getString(R.string.tracks)
            else -> binding.root.context.getString(R.string.tracks_5)
        }
        binding.sumOfTracks.text = String.format(Locale.US, "%d %s", size, track)
    }
}