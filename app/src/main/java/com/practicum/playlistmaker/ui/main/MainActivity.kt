package com.practicum.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.search.activity.SearchActivity
import com.practicum.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.practicum.playlistmaker.ui.settings.activity.SettingsActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val searchBtn = binding.searchBtn
        val mediaBtn = binding.mediaBtn
        val settingsBtn = binding.settingsBtn

        searchBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        mediaBtn.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}