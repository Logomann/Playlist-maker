package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchBtn = findViewById<Button>(R.id.search_btn)
        val mediaBtn = findViewById<Button>(R.id.media_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)

        val searchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.press_search_btn), Toast.LENGTH_SHORT
                ).show()
            }

        }
        searchBtn.setOnClickListener(searchClickListener)
        mediaBtn.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.press_media_btn), Toast.LENGTH_SHORT
            ).show()
        }
        settingsBtn.setOnClickListener {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.press_settings_btn), Toast.LENGTH_SHORT
            ).show()
        }

    }
}