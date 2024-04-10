package com.practicum.playlistmaker.ui.settings.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel


class SettingsActivity : AppCompatActivity() {
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory()
        )[SettingsViewModel::class.java]

        val arrowBackBtn = binding.settingsArrowBackBtn
        val shareBtn = binding.settingsShareBtn
        val supportBtn = binding.settingsSupportBtn
        val agreementBtn = binding.settingsAgreementBtn
        val switchBtn = binding.settingsSwitch


        switchBtn.isChecked = viewModel.getTheme()
        switchBtn.setOnClickListener {
            viewModel.switchTheme()
        }

        arrowBackBtn.setOnClickListener {
            this.finish()
        }
        shareBtn.setOnClickListener {
            viewModel.shareApp()
        }
        supportBtn.setOnClickListener {
            viewModel.openSupport()
        }
        agreementBtn.setOnClickListener {
            viewModel.openTerms()
        }
    }
}