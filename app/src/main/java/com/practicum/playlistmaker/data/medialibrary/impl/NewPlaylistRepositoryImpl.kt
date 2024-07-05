package com.practicum.playlistmaker.data.medialibrary.impl

import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.domain.medialibrary.NewPlaylistRepository
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class NewPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val context: Context
) : NewPlaylistRepository {

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override fun saveImage(filePath: String): Flow<String> = flow {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            (System.currentTimeMillis() / 1000L).toString() + Constants.PICTURE_PNG
        )
        val fileStream = context.contentResolver.openInputStream(filePath.toUri())
        file.writeBytes(fileStream!!.readBytes())
        emit(file.path)
    }


}