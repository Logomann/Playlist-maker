package com.practicum.playlistmaker.domain.sharing


interface ExternalNavigator {
    fun shareLink()
    fun openLink()
    fun openEmail()
}