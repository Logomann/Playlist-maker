package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.medialibrary.FavoriteTracksScreenState
import com.practicum.playlistmaker.ui.medialibrary.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.ui.search.TrackAdapter
import com.practicum.playlistmaker.util.TRACK_KEY
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavoriteTracksFragment : Fragment() {
    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModel<FavoriteTracksViewModel>()
    private var listOfTracks = ArrayList<Track>()
    private val adapter = TrackAdapter(listOfTracks) {
        setOnItemAction(it)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val trackRecyclerView = binding.songRecycleView
        trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        trackRecyclerView.adapter = adapter
        viewModel.render().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is FavoriteTracksScreenState.Content -> {
                    listOfTracks.clear()
                    hidePlaceholder()
                    listOfTracks.addAll(screenState.tracks!!)
                    adapter.notifyDataSetChanged()
                }

                FavoriteTracksScreenState.NoData -> {
                    setPlaceholder()
                    listOfTracks.clear()
                    adapter.notifyDataSetChanged()
                }

                FavoriteTracksScreenState.Loading -> {}
            }
        }
        viewModel.getFavoriteTracks()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteTracks()
    }

    private fun setPlaceholder() {
        binding.fragTrackPlaceholderTv.isVisible = true
        binding.fragmentTracksPlaceholderImage.isVisible = true
    }

    private fun hidePlaceholder() {
        binding.fragTrackPlaceholderTv.isVisible = false
        binding.fragmentTracksPlaceholderImage.isVisible = false
    }

    private fun setOnItemAction(track: Track) {
        val json = Gson().toJson(track)
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerActivity, bundleOf(
                TRACK_KEY to json
            )
        )
    }

    companion object {
        fun newInstance(): FavoriteTracksFragment {
            return FavoriteTracksFragment()
        }
    }
}