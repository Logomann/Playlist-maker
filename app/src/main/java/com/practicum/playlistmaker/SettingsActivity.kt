package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val arrowBackBtn = findViewById<ImageButton>(R.id.settings_arrow_back_btn)
        arrowBackBtn.setOnClickListener {
            val intent = Intent(this,MainActivity :: class.java)
            startActivity(intent)
        }
    }
}