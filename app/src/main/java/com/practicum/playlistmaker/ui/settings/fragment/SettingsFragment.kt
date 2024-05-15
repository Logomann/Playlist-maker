package com.practicum.playlistmaker.ui.settings.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {
    private val viewModel by viewModel<SettingsViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private lateinit var shareBtn: ImageButton
    private lateinit var supportBtn: ImageButton
    private lateinit var agreementBtn: ImageButton
    private lateinit var switchBtn: SwitchMaterial
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        shareBtn = binding.settingsShareBtn
        supportBtn = binding.settingsSupportBtn
        agreementBtn = binding.settingsAgreementBtn
        switchBtn = binding.settingsSwitch
        switchBtn.isChecked = viewModel.getTheme()
        switchBtn.setOnClickListener {
            viewModel.switchTheme()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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