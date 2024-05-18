package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMediaLibraryBinding
import com.practicum.playlistmaker.ui.medialibrary.MediaLibraryAdapter
import com.practicum.playlistmaker.ui.medialibrary.SelectMediaLibraryPage

class MediaLibraryFragment : Fragment(), SelectMediaLibraryPage {
    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!
    private lateinit var tabMediator: TabLayoutMediator
    override fun navigateTo(operation: Int) {
        binding.fragMediaVp.currentItem = operation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        val adapter = MediaLibraryAdapter(hostFragment = this)
        binding.fragMediaVp.adapter = adapter
        tabMediator = TabLayoutMediator(binding.fragMediaTl, binding.fragMediaVp) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
        return binding.root
    }

    override fun onDestroyView() {
        tabMediator.detach()
        _binding = null
        super.onDestroyView()
    }

}