package com.example.myapplication

import FirebaseDatabaseHelper
import RestaurantAdapter
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class ResultsActivity : AppCompatActivity() {

    private lateinit var resultsListView: ListView
    private lateinit var firebaseDatabaseHelper: FirebaseDatabaseHelper
    private lateinit var restaurantAdapter: RestaurantAdapter
    private var restaurantList: List<FirebaseDatabaseHelper.Restaurant> = mutableListOf()
    private lateinit var resultsTextView: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.results)
        resultsTextView = findViewById(R.id.resultsText)



        resultsListView = findViewById(R.id.resultslist)
        firebaseDatabaseHelper = FirebaseDatabaseHelper()

        restaurantAdapter = RestaurantAdapter(this, restaurantList) // Initialize the adapter here

        val query = intent.getStringExtra("query")
        if (query != null) {
            searchRestaurants(query.toLowerCase(Locale.getDefault())) // Convert query to lowercase
            Log.d("ResultsActivity", "Query: $query")
        }

        resultsListView.adapter = restaurantAdapter
    // Set the adapter here
    }

    private fun searchRestaurants(query: String) {
        val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("restaurants")

        // Here we fetch all the restaurants. Adjust this if you want to limit the data fetched.
        val searchQuery: Query = dbRef.orderByChild("name")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allRestaurants = mutableListOf<FirebaseDatabaseHelper.Restaurant>()
                val filteredRestaurants = mutableListOf<FirebaseDatabaseHelper.Restaurant>()

                for (childSnapshot in snapshot.children) {
                    val name = childSnapshot.child("name").getValue(String::class.java) ?: ""
                    val category = childSnapshot.child("cat").getValue(String::class.java) ?: ""
                    val suburb = childSnapshot.child("suburb").getValue(String::class.java) ?: ""
                    val tag = childSnapshot.child("tag").getValue(String::class.java) ?: ""


                    val imageURL = childSnapshot.child("imageURL").getValue(String::class.java) ?: ""
                    val restaurant = FirebaseDatabaseHelper.Restaurant(name, imageURL)

                    // Check if either name or category contains the query
                    if (name.toLowerCase(Locale.getDefault()).contains(query) || category.toLowerCase(Locale.getDefault()).contains(query) || suburb.toLowerCase(Locale.getDefault()).contains(query)
                        || tag.toLowerCase(Locale.getDefault()).contains(query)) {
                        filteredRestaurants.add(restaurant)
                    }
                }

                // Update UI with the filtered data
                restaurantAdapter.updateData(filteredRestaurants)
                val resultsCount = filteredRestaurants.size
                resultsTextView.text = "Results: $resultsCount"

                // Logging the retrieved restaurants
                Log.d("ResultsActivity", "Number of restaurants retrieved: ${filteredRestaurants.size}")
                for (restaurant in filteredRestaurants) {
                    Log.d(
                        "ResultsActivity",
                        "Restaurant Name: ${restaurant.name}, Image URL: ${restaurant.imageURL}"
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("ResultsActivity", "Error fetching restaurants", error.toException())
            }
        }

        searchQuery.addValueEventListener(valueEventListener)
    }}
