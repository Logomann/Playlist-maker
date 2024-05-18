package com.practicum.playlistmaker.data.sharing.impl


import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData


class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun shareLink() {
        val link = context.getString(R.string.practicum_link)
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, link)
        context.startActivity(
            Intent.createChooser(intent, context.getString(R.string.share_app))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun openLink() {
        val link = context.getString(R.string.practicum_offer_url)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(link)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail() {
        val emailData = EmailData(
            context.getString(R.string.email_subject),
            context.getString(R.string.email_body_text),
            context.getString(R.string.supp_email_address)
        )
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data =
            Uri.parse("mailto:${emailData.address}?subject=${emailData.subject}&body=${emailData.body}")
        val createChooserIntent = Intent.createChooser(
            intent,
            context.getString(R.string.text_to_support)
        )
        context.startActivity(createChooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}