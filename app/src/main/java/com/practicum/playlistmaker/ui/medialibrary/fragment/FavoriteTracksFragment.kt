package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.ui.medialibrary.FavoriteTracksScreenState
import com.practicum.playlistmaker.ui.medialibrary.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val hostViewModel by activityViewModel<FavoriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hostViewModel.getFavoriteTracks().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is FavoriteTracksScreenState.Content -> {
                    hidePlaceholder()
                }

                FavoriteTracksScreenState.Default -> {}
                FavoriteTracksScreenState.NoData -> {
                    setPlaceholder()
                }
            }
        }
    }

    private fun setPlaceholder() {
        binding.fragTrackPlaceholderTv.isVisible = true
        binding.fragmentTracksPlaceholderImage.isVisible = true
    }

    private fun hidePlaceholder() {
        binding.fragTrackPlaceholderTv.isVisible = false
        binding.fragmentTracksPlaceholderImage.isVisible = false
    }

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }
}