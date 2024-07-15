package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class RestaurantActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restuarant_page)

        // Find views
        val titleTextView = findViewById<TextView>(R.id.title)
        val imageView = findViewById<ImageView>(R.id.image)
        val breakfastListView = findViewById<ListView>(R.id.breakfast_list)
        val breakfastTitle = findViewById<TextView>(R.id.breakfast)

        // Retrieve data passed through intent
        val restaurantName = intent.getStringExtra("restaurantName")
        val imageUrl = intent.getStringExtra("restaurantImage")

        // Set restaurant name as the title
        titleTextView.text = restaurantName

        // Load image using Picasso
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).fit().centerCrop().into(imageView)
        }

        // Fetch breakfast menu items for the specified restaurant and update the ListView
        val menuReceiver = MenuReceiver()
        menuReceiver.fetchBreakfastItemsForRestaurant(restaurantName!!, object : MenuReceiver.MenuCallback {
            override fun onMenuReceived(menuItems: List<MenuReceiver.MenuItem>, breakfastMenuName: String?) {
                // Log the breakfast items received
                for (item in menuItems) {
                    Log.d("RestaurantActivity", "Received Breakfast Item: ${item.foodname}, Price: ${item.price}, Description: ${item.description}")
                }

                // Set menu name to the TextView
                breakfastTitle.text = breakfastMenuName

                // Create and set adapter for breakfast ListView
                val adapter = MenuAdapter(this@RestaurantActivity, menuItems)
                breakfastListView.adapter = adapter
            }

            override fun onError(errorMessage: String?) {
                // Handle error
                Log.e("RestaurantActivity", "Error fetching menu items: $errorMessage")
            }
        })
    }
}

