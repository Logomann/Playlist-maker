package com.practicum.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.ui.search.TrackAdapter
import com.practicum.playlistmaker.ui.search.TrackHistoryAdapter
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreenState
import com.practicum.playlistmaker.ui.player.activity.AudioPlayerActivity
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {
    companion object {
        private const val EDIT_FIELD = "EDIT_FIELD"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var viewModel: SearchViewModel
    private lateinit var binding: ActivitySearchBinding

    private var editText = ""
    private val listOfTracks = ArrayList<Track>()
    private val adapter = TrackAdapter(listOfTracks) {
        setOnItemAction(it, true)
    }
    private val searchRunnable = Runnable { search() }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var lastQuery = ""
    private lateinit var editField: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderImageNoInternet: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: LinearLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_FIELD, editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText = savedInstanceState.getString(EDIT_FIELD, "")
        editField.setText(editText)
        lastQuery = editText
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]


        val backBtn = binding.searchArrowBackBtn
        backBtn.setOnClickListener {
            this.finish()
        }

        editField = binding.searchEditField
        placeholderImage = binding.searchPlaceholderImage
        placeholderImageNoInternet = binding.searchPlaceholderImageNoInternet
        placeholderText = binding.searchPlaceholderText
        refreshButton = binding.refreshButton
        searchHistory = binding.searchHistoryGroup
        clearHistoryButton = binding.searchCleanHistory
        progressBar = binding.pbTrackSearch
        editField.isFocusableInTouchMode = true
        val clearBtn = binding.searchClearBtn
        val history = viewModel.loadSavedTrackList()

        clearBtn.setOnClickListener {
            editField.text.clear()
            listOfTracks.clear()
            adapter.notifyDataSetChanged()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editField.windowToken, 0)
        }
        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.isVisible = true
                editText = editField.text.toString()
                if (s?.isEmpty() == true) {
                    clearBtn.isVisible = false
                    if (editField.hasFocus() && history.isNotEmpty()) {
                        hidePlaceholder()
                        readTrackHistory()
                        listOfTracks.clear()
                    } else {
                        hideHistory()
                    }
                }
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editField.addTextChangedListener(editTextWatcher)

        val trackRecyclerView = binding.songRecycleView
        trackRecyclerView.layoutManager = LinearLayoutManager(this)
        trackRecyclerView.adapter = adapter

        editField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && history.isNotEmpty() && !viewModel.isCreated()) {
                hidePlaceholder()
                readTrackHistory()
                viewModel.setCreated()
            }
        }

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is SearchScreenState.Content -> {
                    changeContentVisibility(loading = false)
                    if (!screenState.data.isNullOrEmpty()) {
                        listOfTracks.addAll(screenState.data)
                        adapter.notifyDataSetChanged()
                    } else if (screenState.data == null) {
                        setPlaceholderNoInternet()
                    } else {
                        setPlaceholder()
                    }
                }

                is SearchScreenState.Loading -> {
                    listOfTracks.clear()
                    adapter.notifyDataSetChanged()
                    changeContentVisibility(loading = true)
                }

                SearchScreenState.Default -> {}
            }
        }


    }

    private fun changeContentVisibility(loading: Boolean) {
        hideHistory()
        hidePlaceholder()
        binding.pbTrackSearch.isVisible = loading
    }

    private fun setOnItemAction(track: Track, isNotHistory: Boolean) {
        val json = Gson().toJson(track)
        if (isNotHistory) {
            viewModel.saveTrackToHistory(track)
        }
        if (clickDebounce()) {
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK_KEY, json)
            startActivity(intent)
        }

    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (editField.text.isNotEmpty()) {
            lastQuery = editField.text.toString()
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun search() {
        if (lastQuery != viewModel.getRequest()) {
            viewModel.setRequest(lastQuery)
            viewModel.loadTrackList()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPlaceholderNoInternet() {
        listOfTracks.clear()
        adapter.notifyDataSetChanged()
        placeholderImage.isVisible = false
        placeholderText.text = getString(R.string.search_placeholder_text_no_internet)
        placeholderImageNoInternet.isVisible = true
        placeholderText.isVisible = true
        refreshButton.isVisible = true
        refreshButton.setOnClickListener {
            searchDebounce()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPlaceholder() {
        hideHistory()
        listOfTracks.clear()
        adapter.notifyDataSetChanged()
        placeholderText.text = getString(R.string.search_placeholder_text)
        placeholderImage.isVisible = true
        placeholderText.isVisible = true
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readTrackHistory() {
        searchHistory.isVisible = true
        val historyTrackRecyclerView = binding.searchRecyclerHistory
        historyTrackRecyclerView.layoutManager = LinearLayoutManager(this)
        val listOfTracksHistory = viewModel.loadSavedTrackList()

        val historyAdapter = TrackHistoryAdapter(listOfTracksHistory) {
            setOnItemAction(it, false)
        }
        historyTrackRecyclerView.adapter = historyAdapter
        clearHistoryButton.setOnClickListener {
            viewModel.clearSavedTrackList()
            historyAdapter.notifyDataSetChanged()
            searchHistory.isVisible = false
        }
    }

    private fun hidePlaceholder() {
        placeholderImage.isVisible = false
        placeholderText.isVisible = false
        placeholderImageNoInternet.isVisible = false
        refreshButton.isVisible = false
    }

    private fun hideHistory() {
        searchHistory.isVisible = false
        refreshButton.isVisible = false
    }
}
