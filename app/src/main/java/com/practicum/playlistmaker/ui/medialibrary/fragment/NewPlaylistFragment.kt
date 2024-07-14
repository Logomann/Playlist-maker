package com.practicum.playlistmaker.ui.medialibrary.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.ui.medialibrary.NewPlaylistScreenState
import com.practicum.playlistmaker.ui.medialibrary.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.util.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel


open class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentNewPlaylistBinding? = null
    protected val binding get() = _binding!!
    open val viewModel by viewModel<NewPlaylistViewModel>()
    private lateinit var editTextName: TextInputLayout
    private lateinit var editTextDescription: TextInputLayout
    private var isCoverSet = false
    private lateinit var confirmDialog: MaterialAlertDialogBuilder


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        if (requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).isVisible =
                false
        }
        if (requireActivity().findViewById<TextView>(R.id.bottom_tv) != null) {
            requireActivity().findViewById<TextView>(R.id.bottom_tv).isVisible = false
        }
        if (savedInstanceState != null) {
            savedInstanceState.getString(Constants.COVER_KEY)?.let { setCover(it) }
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        editTextName = binding.newPlaylistName
        editTextDescription = binding.newPlaylistDescription
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm_creation_playlist))
            .setMessage(getString(R.string.data_lost_message))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->

            }.setPositiveButton(getString(R.string.complete)) { _, _ ->
                goBack()
            }
        binding.newPlaylistArrow.setOnClickListener {
            getBack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                getBack()
            }
        })
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    setCover(uri.toString())
                }
            }
        binding.newPlaylistCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.newPlaylistCreateBtn.isEnabled = true
                    setColors(editTextName, false)

                } else {
                    binding.newPlaylistCreateBtn.isEnabled = false
                    setColors(editTextName, true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        val textWatcherDescription = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    setColors(editTextDescription, false)
                } else {
                    setColors(editTextDescription, true)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        editTextName.editText?.addTextChangedListener(textWatcher)
        editTextDescription.editText?.addTextChangedListener(textWatcherDescription)

        binding.newPlaylistCreateBtn.setOnClickListener {
            createPlaylist()
        }
        viewModel.render().observe(viewLifecycleOwner) { state ->
            when (state) {
                is NewPlaylistScreenState.Content -> {
                    showMessage(state.playlistName)
                    goBack()
                }

                NewPlaylistScreenState.Default -> {}
                NewPlaylistScreenState.Loading -> {}
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewModel.getUri() != null) {
            outState.putString(Constants.COVER_KEY, viewModel.getUri().toString())
        }

    }

    protected fun setCover(path: String) {
        val cornerRadius = resources.getDimensionPixelSize(R.dimen.track_cover_radius)
        Glide.with(this)
            .load(path)
            .fitCenter()
            .transform(RoundedCorners(cornerRadius))
            .placeholder(R.drawable.placeholder)
            .into(binding.newPlaylistCover)
        binding.newPlaylistCover.setBackgroundResource(0)
        binding.newPlaylistCover.scaleType = ImageView.ScaleType.FIT_XY
        isCoverSet = true
        viewModel.setUri(path.toUri())
    }

    @SuppressLint("ResourceType")
    private fun setColors(layout: TextInputLayout, isEmpty: Boolean) {
        if (isEmpty) {
            requireContext().getColorStateList(R.drawable.new_playlist_et_color).let {
                layout.setBoxStrokeColorStateList(it)
                layout.defaultHintTextColor = it
            }
        } else {
            requireContext().getColorStateList(R.drawable.color_state).let {
                layout.setBoxStrokeColorStateList(it)
                layout.defaultHintTextColor = it
            }
        }

    }

    private fun goBack() {
        if (requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_main_container_view) != null) {
            findNavController().navigateUp()
        } else {
            requireActivity().supportFragmentManager.commit {
                remove(this@NewPlaylistFragment)
            }

        }
    }

    private fun showMessage(message: String) {
        val text =
            getString(R.string.playlist) + " " + message + " " + getString(R.string.created)
        val snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT)
            .setTextColor(requireContext().getColor(R.color.bottom_nav_background))
            .setBackgroundTint(requireContext().getColor(R.color.bottom_nav_color))
            .setTextMaxLines(2)
        val view = snackbar.view
        val textView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    override fun onDestroy() {
        if (requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view) != null) {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).isVisible =
                true
        }
        if (requireActivity().findViewById<TextView>(R.id.bottom_tv) != null) {
            requireActivity().findViewById<TextView>(R.id.bottom_tv).isVisible = true
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        super.onDestroy()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun isDataAvailable(): Boolean {
        return isCoverSet || editTextName.editText?.text!!.isNotEmpty() ||
                editTextDescription.editText?.text!!.isNotEmpty()
    }

    private fun getBack() {
        if (isDataAvailable()) {
            confirmDialog.show()
        } else {
            goBack()
        }
    }

    @SuppressLint("ResourceType")
    private fun createPlaylist() {
        if (isCoverSet) {
            viewModel.saveImage(
                editTextName.editText!!.text.toString(),
                editTextDescription.editText?.text.toString()
            )
        } else {
            viewModel.createPlaylist(
                editTextName.editText!!.text.toString(),
                editTextDescription.editText?.text.toString(), null
            )

        }
    }


}