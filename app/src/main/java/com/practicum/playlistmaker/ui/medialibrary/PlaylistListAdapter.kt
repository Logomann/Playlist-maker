package com.practicum.playlistmaker.ui.medialibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.practicum.playlistmaker.databinding.PlaylistViewBinding
import com.practicum.playlistmaker.domain.model.Playlist

class PlaylistListAdapter(
    private val playlists: List<Playlist>,
    private val onClickItem: (Playlist) -> Unit
) :
    RecyclerView.Adapter<PlaylistListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistListViewHolder {
        val layoutInspector =
            LayoutInflater.from(parent.context)
        return PlaylistListViewHolder(PlaylistViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistListViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onClickItem(playlists[position])
        }
    }
}