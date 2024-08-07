package com.practicum.playlistmaker.ui.medialibrary.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.navigation.fragment.findNavController
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
            viewModel.getData(json)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setName(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }

        val textWatcherDesc = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setDescription(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        binding.newPlaylistName.editText?.addTextChangedListener(textWatcher)
        binding.newPlaylistDescription.editText?.addTextChangedListener(textWatcherDesc)


        viewModel.renderState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is EditPlaylistScreenState.Content -> {
                    setCover(state.playlist.plCover.toString())
                    setText(state.playlist)
                    binding.newPlaylistCreateBtn.setOnClickListener {
                        updatePlaylist(state.playlist)
                        findNavController().navigateUp()
                    }
                    binding.newPlaylistArrow.setOnClickListener {
                        findNavController().navigateUp()
                    }
                }
            }

        }
    }
    private fun setText(playlist: Playlist) {
        binding.newPlaylistName.editText?.setText(playlist.plName)
        binding.newPlaylistDescription.editText?.setText(playlist.plDescription)
        binding.newPlaylistTopTv.text = getString(R.string.edit)
        binding.newPlaylistCreateBtn.text = getString(R.string.save)
        binding.newPlaylistCreateBtn.isEnabled = true
    }

    private fun updatePlaylist(playlist: Playlist) {
        val name = binding.newPlaylistName.editText?.text.toString()
        val description = binding.newPlaylistDescription.editText?.text.toString()
        val cover = viewModel.getUri().toString()
        viewModel.updatePlaylist(playlist, name, description, cover)
    }
}