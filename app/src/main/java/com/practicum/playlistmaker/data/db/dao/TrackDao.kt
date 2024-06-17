package com.practicum.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.data.db.TrackEntity


@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timeCreation DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Int>

}