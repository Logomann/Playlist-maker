package com.practicum.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
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
            val context = holder.itemView.context as AppCompatActivity
            val json = Gson().toJson(tracks[position])
            val intent = Intent(context, AudioPlayerActivity::class.java).apply {
                putExtra("track", json)
            }
            context.startActivity(intent)
        }
    }
}