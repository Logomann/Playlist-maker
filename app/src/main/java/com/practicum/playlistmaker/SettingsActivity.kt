package com.practicum.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBackBtn = findViewById<ImageButton>(R.id.settings_arrow_back_btn)
        val shareBtn = findViewById<ImageButton>(R.id.settings_share_btn)
        val supportBtn = findViewById<ImageButton>(R.id.settings_support_btn)
        val agreementBtn = findViewById<ImageButton>(R.id.settings_agreement_btn)
        val switchBtn = findViewById<SwitchMaterial>(R.id.settings_switch)
        val sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        switchBtn.isChecked = sharedPreferences.getBoolean(THEME_KEY, false)
        switchBtn.setOnCheckedChangeListener { _, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }
        arrowBackBtn.setOnClickListener {
            this.finish()
        }
        shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practicum_link))
            startActivity(Intent.createChooser(intent, null))
        }
        supportBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            val subject = getString(R.string.email_subject)
            val body = getString(R.string.email_body_text)
            val email = getString(R.string.supp_email_address)
            intent.data = Uri.parse("mailto:${email}?subject=${subject}&body=$body")
            startActivity(intent)
        }
        agreementBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.practicum_offer_url))
            startActivity(intent)
        }
    }
}