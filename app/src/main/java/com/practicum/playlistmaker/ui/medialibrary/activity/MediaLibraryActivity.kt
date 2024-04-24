package com.practicum.playlistmaker.ui.medialibrary.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaLibraryBinding
import com.practicum.playlistmaker.ui.medialibrary.fragment.MediaLibraryFragment

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fragment_media_library_container_view, MediaLibraryFragment())
            }

        }
        binding.mediaArrowBackBtn.setOnClickListener {
            this.finish()
        }
    }
}