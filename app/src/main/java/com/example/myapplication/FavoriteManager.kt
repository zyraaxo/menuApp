package com.example.myapplication

object FavoriteManager {
    private val favoriteRestaurants = mutableListOf<String>()

    fun addFavorite(restaurant: String) {
        if (!favoriteRestaurants.contains(restaurant)) {
            favoriteRestaurants.add(restaurant)
        }
    }

    fun removeFavorite(restaurant: String) {
        favoriteRestaurants.remove(restaurant)
    }

    fun getFavorites(): List<String> {
        return favoriteRestaurants
    }

    fun isFavorite(restaurant: String): Boolean {
        return favoriteRestaurants.contains(restaurant)
    }
}
