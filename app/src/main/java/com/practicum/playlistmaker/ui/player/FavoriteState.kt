package com.practicum.playlistmaker.ui.player

sealed class FavoriteState {
    data object Favorite : FavoriteState()
    data object NotFavorite : FavoriteState()
}