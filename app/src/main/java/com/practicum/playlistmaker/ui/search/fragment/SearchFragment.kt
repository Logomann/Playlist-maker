package com.practicum.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.TRACK_KEY
import com.practicum.playlistmaker.ui.search.TrackAdapter
import com.practicum.playlistmaker.ui.search.TrackHistoryAdapter
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.model.track.model.Track
import com.practicum.playlistmaker.ui.search.SearchScreenState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    companion object {
        private const val EDIT_FIELD = "EDIT_FIELD"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel by viewModel<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackSearchDebounce: (String, String) -> Unit
    private lateinit var onTrackClickDebounce: (Track, Boolean) -> Unit

    private var editText = ""
    private val listOfTracks = ArrayList<Track>()
    private val adapter = TrackAdapter(listOfTracks) {
        onTrackClickDebounce(it, true)
    }
    private var lastQuery = ""
    private lateinit var editField: EditText
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderImageNoInternet: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var refreshButton: Button
    private lateinit var searchHistory: LinearLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar
    private var listOfHistory = listOf<Track>()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_FIELD, editText)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getHistoryTrackList()
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        editField = binding.searchEditField
        placeholderImage = binding.searchPlaceholderImage
        placeholderImageNoInternet = binding.searchPlaceholderImageNoInternet
        placeholderText = binding.searchPlaceholderText
        refreshButton = binding.refreshButton
        searchHistory = binding.searchHistoryGroup
        clearHistoryButton = binding.searchCleanHistory
        progressBar = binding.pbTrackSearch
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editField.isFocusableInTouchMode = true
        val clearBtn = binding.searchClearBtn


        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track, isHistory ->
            setOnItemAction(track, isHistory)
        }
        trackSearchDebounce = debounce(
            SEARCH_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            true
        ) { _, _ ->
            searchDebounce()
        }
        clearBtn.setOnClickListener {
            editField.text.clear()
            listOfTracks.clear()
            adapter.notifyDataSetChanged()
            if (listOfHistory.isNotEmpty()) {
                readTrackHistory()
            }
            viewModel.clearData()
            val inputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editField.windowToken, 0)
        }
        val editTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearBtn.isVisible = true
                editText = editField.text.toString()
                if (s?.isEmpty() == true) {
                    clearBtn.isVisible = false
                    if (editField.hasFocus() && listOfHistory.isNotEmpty()) {
                        hidePlaceholder()
                        readTrackHistory()
                        listOfTracks.clear()
                    } else {
                        hideHistory()
                    }
                }
                trackSearchDebounce(editText, editText)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        editField.addTextChangedListener(editTextWatcher)

        val trackRecyclerView = binding.songRecycleView
        trackRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        trackRecyclerView.adapter = adapter

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchScreenState.Content -> {
                    changeContentVisibility(loading = false)
                    listOfTracks.addAll(screenState.data!!)
                    adapter.notifyDataSetChanged()
                    hideHistory()
                }

                SearchScreenState.Loading -> {
                    listOfTracks.clear()
                    adapter.notifyDataSetChanged()
                    changeContentVisibility(loading = true)
                }

                SearchScreenState.NoData -> {
                    changeContentVisibility(loading = false)
                    setPlaceholder()
                }

                SearchScreenState.NoInternet -> {
                    changeContentVisibility(loading = false)
                    setPlaceholderNoInternet()
                }

                SearchScreenState.Default -> {
                    setOnFocusListener()
                }
            }
        }
        listOfHistory = viewModel.history.value

    }

    override fun onResume() {
        super.onResume()
        viewModel.getHistoryTrackList()
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
        findNavController().navigate(
            R.id.action_searchFragment_to_audioPlayerActivity, bundleOf(
                TRACK_KEY to json
            )
        )
    }

    private fun searchDebounce() {
        if (editField.text.isNotEmpty()) {
            lastQuery = editField.text.toString()
            search()
        }
    }

    private fun setOnFocusListener() {
        editField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                if (listOfHistory.isNotEmpty()) {
                    hidePlaceholder()
                    readTrackHistory()
                }
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun search() {
        viewModel.setRequest(lastQuery)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setPlaceholderNoInternet() {
        listOfTracks.clear()
        adapter.notifyDataSetChanged()
        placeholderImage.isVisible = false
        placeholderText.text = getString(R.string.search_placeholder_text_no_internet)
        placeholderImageNoInternet.isVisible = true
        placeholderText.isVisible = true
        viewModel.setRefresh(true)
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
        historyTrackRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val historyAdapter = TrackHistoryAdapter(listOfHistory) {
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
