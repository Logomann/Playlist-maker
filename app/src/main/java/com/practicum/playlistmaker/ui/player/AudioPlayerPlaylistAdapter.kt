package com.practicum.playlistmaker.ui.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistViewAudioplayerBinding
import com.practicum.playlistmaker.domain.model.Playlist

class AudioPlayerPlaylistAdapter(
    private val playlists: List<Playlist>,
    private val onClickItem: (Playlist) -> Unit
) : RecyclerView.Adapter<AudioPlayerPlaylistViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AudioPlayerPlaylistViewHolder {
        val layoutInspector =
            LayoutInflater.from(parent.context)

        return AudioPlayerPlaylistViewHolder(
            PlaylistViewAudioplayerBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: AudioPlayerPlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onClickItem(playlists[position])
        }
    }
}