package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.presentation.ui.audioplayer.AudioPlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val EDIT_FIELD = "EDIT_FIELD"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var editText = ""
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val listOfTracks = ArrayList<Track>()
    private val adapter = TrackAdapter(listOfTracks) {
        setOnItemAction(it, true)
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private lateinit var preferences: SharedPreferences
    private val searchRunnable = Runnable { search("") }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val iTunesService = retrofit.create(ITunesApi::class.java)
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
        setContentView(R.layout.activity_search)

        val backBtn = findViewById<ImageButton>(R.id.search_arrow_back_btn)
        backBtn.setOnClickListener {
            this.finish()
        }
        editField = findViewById(R.id.search_edit_field)
        placeholderImage = findViewById(R.id.search_placeholder_image)
        placeholderImageNoInternet = findViewById(R.id.search_placeholder_image_no_internet)
        placeholderText = findViewById(R.id.search_placeholder_text)
        refreshButton = findViewById(R.id.refresh_button)
        searchHistory = findViewById(R.id.search_history_group)
        clearHistoryButton = findViewById(R.id.search_clean_history)
        preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        progressBar = findViewById(R.id.pbTrackSearch)
        editField.isFocusableInTouchMode = true
        val clearBtn = findViewById<ImageButton>(R.id.search_clear_btn)
        val history = SearchHistory(preferences).read()

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

        val songRecyclerView = findViewById<RecyclerView>(R.id.song_recycle_view)
        songRecyclerView.layoutManager = LinearLayoutManager(this)
        songRecyclerView.adapter = adapter

        editField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && history.isNotEmpty()) {
                hidePlaceholder()
                readTrackHistory()
            }
        }
    }

    private fun setOnItemAction(track: Track, isNotHistory: Boolean) {
        if (isNotHistory) {
            val preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
            val history = SearchHistory(preferences)
            history.addTrack(track)
        }
        if (clickDebounce()) {
            val json = Gson().toJson(track)
            val intent = Intent(this, AudioPlayerActivity::class.java)
            intent.putExtra(TRACK, json)
            startActivity(intent)
        }

    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        if (editField.text.isNotEmpty()) {
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

    private fun search(query: String) {
        listOfTracks.clear()
        hideHistory()
        hidePlaceholder()
        progressBar.isVisible = true
        lastQuery = query.ifEmpty {
            editField.text.toString()
        }
        iTunesService.findTrack(lastQuery).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                progressBar.isVisible = false
                val responseText = response.body()?.results

                if (response.isSuccessful) {
                    if (!responseText.isNullOrEmpty()) {
                        listOfTracks.addAll(responseText)
                        adapter.notifyDataSetChanged()
                    } else {
                        placeholderText.text = getString(R.string.search_placeholder_text)
                        placeholderImage.isVisible = true
                        placeholderText.isVisible = true
                    }
                } else {
                    hideHistory()
                    setPlaceholderNoInternet()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                progressBar.isVisible = false
                hideHistory()
                setPlaceholderNoInternet()
            }
        })

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
            search(lastQuery.ifEmpty {
                editField.text.toString()
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readTrackHistory() {
        searchHistory.isVisible = true
        val historyTrackRecyclerView = findViewById<RecyclerView>(R.id.search_recycler_history)
        historyTrackRecyclerView.layoutManager = LinearLayoutManager(this)
        val history = SearchHistory(preferences)
        val listOfTracksHistory = history.read()
        val historyAdapter = TrackHistoryAdapter(listOfTracksHistory) {
            setOnItemAction(it, false)
        }
        historyTrackRecyclerView.adapter = historyAdapter
        clearHistoryButton.setOnClickListener {
            history.clear()
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