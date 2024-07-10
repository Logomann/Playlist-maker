package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.medialibrary.PlaylistScreenState
import com.practicum.playlistmaker.ui.medialibrary.PlaylistTrackAdapter
import com.practicum.playlistmaker.ui.medialibrary.view_model.PlaylistViewModel
import com.practicum.playlistmaker.util.PLAYLIST_KEY
import com.practicum.playlistmaker.util.TRACK_KEY
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class PlaylistFragment : Fragment() {
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private val listOfTracks = ArrayList<Track>()
    private val adapter = PlaylistTrackAdapter(listOfTracks, {
        setOnclickItem(it)
    }, {
        setOnLongClickItem(it)
    })
    private var sumOfTracks = ""
    private lateinit var playlist: Playlist

    override fun onResume() {
        super.onResume()
        if (requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).isVisible =
                false
        }
        viewModel.getTracks()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        if (requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).isVisible =
                false
        }
        arguments?.let { viewModel.setPlaylist(it.getString(PLAYLIST_KEY).toString()) }
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetContainer = binding.playlistBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        val display = requireContext().resources.displayMetrics
        bottomSheetBehavior.peekHeight = display.heightPixels / 3

        val menuBottomSheetContainer = binding.playlistMenuBottomSheet
        menuBottomSheetBehavior = BottomSheetBehavior.from(menuBottomSheetContainer)
        menuBottomSheetBehavior.peekHeight = display.heightPixels / 3
        binding.playlistArrow.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistShareBtn.setOnClickListener {
            sharePlaylist()

        }
        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                        binding.overlay.isVisible = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        binding.playlistMenuBtn.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.playlistShareLl.setOnClickListener {
            sharePlaylist()
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.deletePlaylistLl.setOnClickListener {
            deletePlaylist()
        }
        binding.editInfoLl.setOnClickListener {
            val json = Gson().toJson(playlist)
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlaylistFragment, bundleOf(
                    PLAYLIST_KEY to json
                )
            )
        }

        val trackRecyclerView = binding.playlistBottomSheetRecycler
        trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        trackRecyclerView.adapter = adapter

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistScreenState.Content -> {
                    playlist = state.playlist
                    Glide.with(this)
                        .load(state.playlist.plCover)
                        .fitCenter()
                        .placeholder(R.drawable.placeholder)
                        .into(binding.playlistCoverIv)
                    binding.playlistCoverIv.scaleType = ImageView.ScaleType.FIT_XY
                    binding.playlistName.text = state.playlist.plName
                    binding.playlistDescription.text = state.playlist.plDescription

                    val minutes = requireContext().resources.getQuantityString(
                        R.plurals.time_of_tracks_quantity, state.timeOfTracks
                    )
                    binding.playlistSumOfTime.text = String.format(
                        Locale.getDefault(),
                        "%d %s",
                        state.timeOfTracks,
                        minutes
                    )

                    sumOfTracks = requireContext().resources.getQuantityString(
                        R.plurals.track_quantity,
                        state.playlist.plTracksIDs.size
                    )
                    val tracks = String.format(
                        Locale.getDefault(),
                        "%d %s",
                        state.playlist.plTracksIDs.size,
                        sumOfTracks
                    )
                    binding.playlistSumOfTracks.text = tracks
                    binding.sumOfTracks.text = tracks
                    binding.nameOfPlaylist.text = state.playlist.plName
                    val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
                    Glide.with(this)
                        .load(state.playlist.plCover)
                        .fitCenter()
                        .transform(RoundedCorners(cornerRadius))
                        .placeholder(R.drawable.placeholder)
                        .into(binding.playlistImage)
                    listOfTracks.clear()
                    listOfTracks.addAll(state.tracks)
                    listOfTracks.reverse()
                    binding.noTracksInPlaylistTv.isVisible = listOfTracks.isEmpty()
                    adapter.notifyDataSetChanged()
                }

                PlaylistScreenState.Deleted -> {
                    findNavController().navigateUp()
                }
            }

        }

    }

    override fun onDestroy() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility =
            View.VISIBLE
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setOnclickItem(track: Track) {
        val json = Gson().toJson(track)
        findNavController().navigate(
            R.id.action_playlistFragment_to_audioPlayerActivity, bundleOf(
                TRACK_KEY to json
            )
        )

    }


    private fun setOnLongClickItem(track: Track) {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_track))
            .setMessage(getString(R.string.delete_track_confirm))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }.setPositiveButton(getString(R.string.delete)) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)
            }
        confirmDialog.show()

    }

    private fun deletePlaylist() {
        menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.delete_playlist))
            .setMessage(getString(R.string.want_to_delete_playlist) + " \"${binding.playlistName.text}\"?")
            .setNegativeButton(getString(R.string.no)) { _, _ ->

            }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist()
            }
        confirmDialog.show()
    }

    private fun showMessage() {
        val text = getString(R.string.no_tracks_in_playlist)
        val snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setTextColor(requireContext().getColor(R.color.bottom_nav_background))
            .setBackgroundTint(requireContext().getColor(R.color.bottom_nav_color))
            .setTextMaxLines(2)
        val view = snackbar.view
        val textView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    private fun sharePlaylist() {
        if (listOfTracks.isEmpty()) {
            showMessage()
        } else {
            viewModel.sharePlaylist(sumOfTracks)
        }
    }
}