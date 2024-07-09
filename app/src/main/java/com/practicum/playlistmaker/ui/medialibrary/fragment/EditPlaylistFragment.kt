package com.practicum.playlistmaker.ui.medialibrary.fragment


import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.medialibrary.EditPlaylistScreenState
import com.practicum.playlistmaker.ui.medialibrary.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.util.PLAYLIST_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : NewPlaylistFragment() {
    override val viewModel by viewModel<EditPlaylistViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val json = it.getString(PLAYLIST_KEY)
            val type = object : TypeToken<Playlist>() {}.type
            val playlist: Playlist = Gson().fromJson(json, type)
            viewModel.getData(playlist)
        }

        viewModel.renderState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is EditPlaylistScreenState.Content -> {
                    setCover(state.playlist.plCover!!)
                    binding.newPlaylistName.editText!!.setText(state.playlist.plName)
                    binding.newPlaylistDescription.editText!!.setText(state.playlist.plDescription)
                    binding.newPlaylistTopTv.text = getString(R.string.edit)
                    binding.newPlaylistCreateBtn.text = getString(R.string.save)
                    binding.newPlaylistCreateBtn.isEnabled = true
                    binding.newPlaylistCreateBtn.setOnClickListener {
                        val playlist = Playlist(
                            plId = state.playlist.plId,
                            plName = binding.newPlaylistName.editText!!.text.toString(),
                            plDescription = binding.newPlaylistDescription.editText!!.text.toString(),
                            plCover = coverUri.toString(),
                            plTracksIDs = state.playlist.plTracksIDs
                        )
                        viewModel.updatePlaylist(playlist)
                        findNavController().navigateUp()
                    }
                    binding.newPlaylistArrow.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }
            }

        }
    }

}