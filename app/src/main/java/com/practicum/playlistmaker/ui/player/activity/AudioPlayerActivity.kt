package com.practicum.playlistmaker.ui.player.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.model.Playlist
import com.practicum.playlistmaker.ui.medialibrary.PlaylistsScreenState
import com.practicum.playlistmaker.ui.medialibrary.fragment.NewPlaylistFragment
import com.practicum.playlistmaker.ui.player.AddPlaylistState
import com.practicum.playlistmaker.ui.player.AudioPlayerPlaylistAdapter
import com.practicum.playlistmaker.ui.player.AudioPlayerScreenState
import com.practicum.playlistmaker.ui.player.FavoriteState
import com.practicum.playlistmaker.ui.player.view_model.AudioPlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AudioPlayerActivity : AppCompatActivity() {
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var binding: ActivityAudioPlayerBinding
    private var playButton: ImageButton? = null
    private var playingTime: TextView? = null
    private val playlists = ArrayList<Playlist>()
    private val adapter = AudioPlayerPlaylistAdapter(playlists) {
        setOnItemClick(it)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetContainer = binding.standardBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        viewModel.getPlaylists()
        viewModel.render().observe(this) { state ->
            when (state) {
                is PlaylistsScreenState.Content -> {
                    playlists.clear()
                    playlists.addAll(state.playlist)
                    adapter.notifyDataSetChanged()
                }

                PlaylistsScreenState.Default -> {}
                PlaylistsScreenState.Loading -> {}
                PlaylistsScreenState.NoData -> {}
            }
        }
        val playlistsRecyclerView = binding.bottomSheetRecycler
        playlistsRecyclerView.layoutManager = LinearLayoutManager(this)
        playlistsRecyclerView.adapter = adapter

        bottomSheetBehavior.addBottomSheetCallback(object :
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.addToListBtn.setOnClickListener {
            viewModel.getPlaylists()
            binding.overlay.isVisible = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val trackCover = binding.trackCover
        val bckBtn = binding.audioPlayerArrow
        bckBtn.setOnClickListener {
            finish()
        }
        playButton = binding.trackPlayBtn
        playButton?.isEnabled = false
        playButton?.setOnClickListener {
            viewModel.playBack()
        }

        playingTime = binding.playingTime
        val url: String?
        val track = viewModel.getTrack(intent.getStringExtra(TRACK_KEY))
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
        val trackUrl = viewModel.getImage(track.artworkUrl100)

        Glide.with(this)
            .load(trackUrl)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(trackCover)

        val trackName = binding.songName
        trackName.text = track.trackName
        url = track.previewUrl


        val artistName = binding.artistName
        artistName.text = track.artistName

        val duration = binding.trackDuration
        duration.text = track.trackTimeMillis


        val albumGroup = binding.albumGroup

        if (track.collectionName.isNotEmpty()) {
            albumGroup.isVisible = true
            val album = binding.albumName
            album.text = track.collectionName
        } else {
            albumGroup.isVisible = false
        }

        val year = binding.trackYear
        year.text = track.releaseDate

        val genre = binding.trackGenre
        genre.text = track.primaryGenreName

        val country = binding.countryName
        country.text = track.country
        val isFavorite = binding.likeBtn
        isFavorite.setOnClickListener {
            viewModel.onFavoriteClicked()
        }
        viewModel.isFavorite().observe(this) { favoriteState ->
            when (favoriteState) {
                FavoriteState.Favorite -> {
                    isFavorite.isSelected = true
                }

                FavoriteState.NotFavorite -> {
                    isFavorite.isSelected = false
                }
            }
        }
        if (!url.isNullOrEmpty()) {
            viewModel.getScreenStateLiveData(url).observe(this) { screenState ->
                when (screenState) {
                    AudioPlayerScreenState.Prepared -> {
                        playButton?.isEnabled = true
                    }

                    AudioPlayerScreenState.Completed -> {
                        playingTime?.text = getString(R.string.start_time_00)
                        playButton?.setImageResource(R.drawable.track_play_btn)
                    }

                    is AudioPlayerScreenState.Playing -> {
                        startPlayer()
                        playingTime?.text = screenState.progress
                    }

                    AudioPlayerScreenState.Paused -> {
                        playButton?.isEnabled = true
                        pausePlayer()
                    }

                    else -> {}
                }
            }
        }
        binding.addPlaylistBtn.setOnClickListener {
            supportFragmentManager.commit {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                add(R.id.fragment_container_view, NewPlaylistFragment())
            }
        }
        viewModel.onPlayListClickedRender().observe(this) { state ->
            when (state) {
                is AddPlaylistState.Added -> {
                    showMessage(true, state.playlistName)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }

                is AddPlaylistState.AlreadyExist -> {
                    showMessage(false, state.playlistName)
                }
            }
        }


    }

    private fun setOnItemClick(playlist: Playlist) {
        viewModel.onPlaylistClicked(playlist)
    }


    @SuppressLint("StringFormatInvalid")
    private fun showMessage(isAdded: Boolean, playlistName: String) {
        val text = if (isAdded) {
            getString(R.string.track_added, playlistName)
        } else {
            getString(R.string.track_already_in_playlist, playlistName)
        }
        val snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setTextColor(getColor(R.color.bottom_nav_background))
            .setBackgroundTint(getColor(R.color.bottom_nav_color))
            .setTextMaxLines(2)
        val view = snackbar.view
        val textView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }


    private fun startPlayer() {
        playButton?.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        playButton?.setImageResource(R.drawable.track_play_btn)
    }


    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

}

