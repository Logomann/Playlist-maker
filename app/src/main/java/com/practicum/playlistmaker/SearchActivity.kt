package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    companion object {
        private const val EDIT_FIELD = "EDIT_FIELD"
    }

    private var editText = ""
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val listOfTracks = ArrayList<Track>()
    private val adapter = TrackAdapter(listOfTracks)
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    private lateinit var editField: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderImageNoInternet: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var refreshButton: Button
    private lateinit var lastQuery: String
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
        val clearBtn = findViewById<ImageButton>(R.id.search_clear_btn)
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
                clearBtn.visibility = View.VISIBLE
                editText = editField.text.toString()
                if (editField.text.isEmpty()) {
                    clearBtn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        editField.addTextChangedListener(editTextWatcher)

        val songRecyclerView = findViewById<RecyclerView>(R.id.song_recycle_view)
        songRecyclerView.layoutManager = LinearLayoutManager(this)
        songRecyclerView.adapter = adapter

        editField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search("")
            }
            false
        }

    }

    private fun search(query: String) {
        iTunesService.findTrack(query.ifEmpty {
            editField.text.toString()
        }).enqueue(object : Callback<TrackResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<TrackResponse>,
                response: Response<TrackResponse>
            ) {
                val responseText = response.body()?.results
                listOfTracks.clear()
                placeholderImage.isVisible = false
                placeholderText.isVisible = false
                placeholderImageNoInternet.isVisible = false
                refreshButton.isVisible = false
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
                    setPlaceholderNoInternet()
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
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
        refreshButton.setOnClickListener { search(lastQuery) }
    }

}