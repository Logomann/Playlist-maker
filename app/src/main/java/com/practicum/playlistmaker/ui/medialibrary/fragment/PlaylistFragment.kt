package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.ui.medialibrary.PlaylistsScreenState
import com.practicum.playlistmaker.ui.medialibrary.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val hostViewModel by activityViewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hostViewModel.getPlaylists().observe(viewLifecycleOwner) {screenState ->
            when ( screenState) {
                is PlaylistsScreenState.Content -> {
                    hidePlaceholder()
                }
                PlaylistsScreenState.Default -> {}
                PlaylistsScreenState.NoData -> {
                    setPlaceholder()
                }
            }

        }
    }
    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
    private fun setPlaceholder() {
        binding.fragPlaylistBtn.isVisible = true
        binding.fragPlaylistTv.isVisible = true
        binding.fragmentPlaylistPlaceholderImage.isVisible = true
    }
    private fun hidePlaceholder() {
        binding.fragPlaylistBtn.isVisible = false
        binding.fragPlaylistTv.isVisible = false
        binding.fragmentPlaylistPlaceholderImage.isVisible = false
    }
    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }
}