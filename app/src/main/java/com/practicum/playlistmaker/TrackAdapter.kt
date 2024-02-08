package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class TrackAdapter(
    private val tracks: List<Track>, private val onClickItem: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.track_view,
            parent, false
        )
        return TrackViewHolder(view)
    }

    override fun getItemCount() = tracks.size


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            val preferences = holder.itemView.context.getSharedPreferences(
                PREFERENCES,
                AppCompatActivity.MODE_PRIVATE
            )
            val history = SearchHistory(preferences)
            history.addTrack(tracks[position])
            onClickItem(tracks[position])
        }
    }
}