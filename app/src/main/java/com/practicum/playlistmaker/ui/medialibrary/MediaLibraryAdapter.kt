package com.practicum.playlistmaker.ui.medialibrary

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.ui.medialibrary.fragment.FavoriteTracksFragment
import com.practicum.playlistmaker.ui.medialibrary.fragment.PlaylistFragment

class MediaLibraryAdapter(hostFragment: Fragment) : FragmentStateAdapter(hostFragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            1 -> PlaylistFragment.newInstance()
            else -> FavoriteTracksFragment.newInstance()
        }
    }
}