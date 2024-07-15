package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var restaurantRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        FirebaseApp.initializeApp(this)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance()
        restaurantRef = database.getReference("restaurants")

        // Fetch restaurants from Realtime Database
        fetchRestaurantsFromRealtimeDatabase()


        val dashText: TextView = findViewById(R.id.dashText)
        dashText.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
    }




    private fun fetchRestaurantsFromRealtimeDatabase() {
        restaurantRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val restaurants = mutableListOf<Restaurant>()
                for (childSnapshot in snapshot.children) {
                    val restaurant = childSnapshot.getValue(Restaurant::class.java)
                    restaurant?.let {
                        restaurants.add(it)
                    }
                }
                printRestaurants(restaurants)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    // Function to print the list of restaurants
    private fun printRestaurants(restaurants: List<Restaurant>) {
        for (restaurant in restaurants) {
            println("Restaurant: ${restaurant.name}, Image URL: ${restaurant.imageURL}")
        }
    }

    // Restaurant data class
    data class Restaurant(
        val name: String = "",
        val imageURL: String = ""
    )
}



