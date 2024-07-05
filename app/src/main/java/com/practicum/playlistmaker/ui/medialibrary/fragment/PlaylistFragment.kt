package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.medialibrary.PlaylistAdapter
import com.practicum.playlistmaker.ui.medialibrary.PlaylistsScreenState
import com.practicum.playlistmaker.ui.medialibrary.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel by viewModel<PlaylistViewModel>()
    private val playlists = ArrayList<Playlist>()
    private val adapter = PlaylistAdapter(playlists)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragPlaylistBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }
        val playlistsRecyclerView = binding.playlistRecyclerView
        playlistsRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        playlistsRecyclerView.adapter = adapter

        hostViewModel.render().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlaylistsScreenState.Content -> {
                    hidePlaceholder()
                    playlists.clear()
                    playlists.addAll(screenState.playlist)
                    adapter.notifyDataSetChanged()

                }

                PlaylistsScreenState.Default -> {}
                PlaylistsScreenState.NoData -> {
                    setPlaceholder()
                }

                PlaylistsScreenState.Loading -> {}
            }

        }
        hostViewModel.getPlaylists()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        hostViewModel.getPlaylists()
    }

    private fun setPlaceholder() {
        binding.fragPlaylistTv.isVisible = true
        binding.fragmentPlaylistPlaceholderImage.isVisible = true
    }

    private fun hidePlaceholder() {
        binding.fragPlaylistTv.isVisible = false
        binding.fragmentPlaylistPlaceholderImage.isVisible = false
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }
}