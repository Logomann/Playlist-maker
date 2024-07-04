package com.practicum.playlistmaker.ui.medialibrary

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.domain.model.Playlist
import java.util.Locale

class PlaylistViewHolder(private val binding: PlaylistViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.radius_8px)

        Glide.with(binding.root)
            .load(playlist.plCover)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(binding.playlistCover)


        binding.newPlaylistName.text = playlist.plName
        val size = playlist.plTracksIDs.size
        val track = when (size) {
            1 -> binding.root.context.getString(R.string.track)
            in 2..4 -> binding.root.context.getString(R.string.tracks)
            else -> binding.root.context.getString(R.string.tracks_5)
        }
        binding.playlistSize.text = String.format(Locale.US, "%d %s", size, track)
    }
}