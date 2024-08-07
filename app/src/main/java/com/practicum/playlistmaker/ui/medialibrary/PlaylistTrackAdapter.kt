package com.practicum.playlistmaker.ui.medialibrary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.search.TrackViewHolder

class PlaylistTrackAdapter(
    private val tracks: List<Track>, private val onClickItem: (Track) -> Unit,
    private val onLongClickItem: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(
            TrackViewBinding.inflate(layoutInspector, parent, false)
        )

    }

    override fun getItemCount() = tracks.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onClickItem(tracks[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClickItem(tracks[position])
            return@setOnLongClickListener true
        }
    }
}
