package com.practicum.playlistmaker.data.sharing.impl


import android.app.Application
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData


class ExternalNavigatorImpl(private val application: Application) : ExternalNavigator {
    override fun shareLink() {
        val link = application.getString(R.string.practicum_link)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openLink() {
        val link = application.getString(R.string.practicum_offer_url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.startActivity(intent)
    }

    override fun openEmail() {
        val emailData = EmailData(
            application.getString(R.string.email_subject),
            application.getString(R.string.email_body_text),
            application.getString(R.string.supp_email_address)
        )
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data =
            Uri.parse("mailto:${emailData.address}?subject=${emailData.subject}&body=${emailData.body}")
        application.startActivity(intent)
    }

}