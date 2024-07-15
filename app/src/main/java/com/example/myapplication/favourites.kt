package com.example.myapplication


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.FavoriteManager
import com.example.myapplication.R

class FavouritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favourites)

        // Assuming FavoriteManager is a singleton managing the favorite restaurants
        val favoriteRestaurants = FavoriteManager.getFavorites()

        // Set up the ListView with an adapter
        val listView: ListView = findViewById(R.id.favView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, favoriteRestaurants)
        listView.adapter = adapter
    }
}
