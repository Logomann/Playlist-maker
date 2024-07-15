package com.practicum.playlistmaker.data.db.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.TracksInPlaylistsEntity

@Dao
interface TracksInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TracksInPlaylistsEntity)

    @Query("SELECT * FROM tracks_in_playlists_table")
    suspend fun getTracks(): List<TracksInPlaylistsEntity>

    @Delete
    suspend fun deleteTrack(track: TracksInPlaylistsEntity)

    @Query("SELECT * FROM tracks_in_playlists_table WHERE trackId =:trackId")
    suspend fun getTrack(trackId: Int): TracksInPlaylistsEntity
}