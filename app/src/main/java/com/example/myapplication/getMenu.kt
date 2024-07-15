package com.example.myapplication

import com.google.firebase.database.*
import java.util.Locale

class getMenu {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    fun getRestaurants(callback: (List<Restaurant>) -> Unit) {
        val dbRef: DatabaseReference = database.getReference("restaurants")

        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val restaurants = mutableListOf<Restaurant>()

                for (restaurantSnapshot in snapshot.children) {
                    val menuItems = mutableListOf<MenuItem>()
                    val menuSnapshot = restaurantSnapshot.child("breakfast")
                    for (menuItemSnapshot in menuSnapshot.children) {
                        val menuItem = menuItemSnapshot.getValue(MenuItem::class.java)
                        menuItem?.let {
                            menuItems.add(it)
                        }
                    }

                    val restaurant = restaurantSnapshot.getValue(Restaurant::class.java)
                    restaurant?.let {
                        restaurants.add(it.copy(menuItems = menuItems))
                    }
                }

                callback(restaurants)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                callback(emptyList())
            }
        })
    }
}
data class Restaurant(
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val imageURL: String = "",
    val suburb: String = "",
    val tag: String = "",
    val weekdayHours: String = "",
    val weekendHours: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val menuItems: List<MenuItem> = emptyList() // List of menu items
)
data class MenuItem(
    val name: String = "",
    val price: Double = 0.0,
    val category: String = ""
)


