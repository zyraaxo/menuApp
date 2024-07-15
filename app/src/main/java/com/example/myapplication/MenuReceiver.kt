package com.example.myapplication

import android.content.Intent
import android.util.Log
import android.widget.TextView
import com.google.firebase.database.*

class MenuReceiver {

    private val menuRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("restaurants")
    private val menuNamesList = mutableListOf<String>() // Public list to hold menu names


    // Data model for MenuItem
    data class MenuItem(
        val foodname: String = "",
        val description: String = "",
        val price: Double = 0.0
    )

    // Callback interface
    interface MenuCallback {
        fun onMenuReceived(menuItems: List<MenuItem>, menuName: String?)
        fun onError(errorMessage: String?)
    }

    // Function to fetch breakfast menu items for a specific restaurant from Realtime Database
    fun fetchBreakfastItemsForRestaurant(restaurantName: String, callback: MenuCallback) {
        val breakfastItemsList = mutableListOf<MenuItem>()

        // Query to find the restaurant based on the restaurant name
        menuRef.orderByChild("name").equalTo(restaurantName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val restaurantSnapshot = dataSnapshot.children.iterator().next()
                        val restaurantDataRef = restaurantSnapshot.ref // Reference to the specific restaurant's data

                        // Now you can fetch the menu items for this restaurant
                        val menuRef = restaurantDataRef.child("menu")
                        val breakfastMenuRef = menuRef.child("breakfast")
                        val breakfastMenuName =
                            restaurantSnapshot.child("menu/breakfast/menuname").getValue(
                                String::class.java
                            )

                        Log.d("MenuReceiver", "Fetching breakfast menu items for $restaurantName")

                        // Fetch the breakfast menu items
                        breakfastMenuRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (childSnapshot in snapshot.children) {
                                    val name = childSnapshot.child("foodname").getValue(String::class.java) ?: ""
                                    val description = childSnapshot.child("descripton").getValue(String::class.java) ?: ""
                                    val price = childSnapshot.child("price").getValue(Double::class.java) ?: 0.0
                                    val menuItem = MenuItem(name, description, price)
                                    breakfastItemsList.add(menuItem)

                                    Log.d("MenuReceiver", "Received Breakfast Item: $name, Description: $description, Price: $price")
                                }
                                callback.onMenuReceived(breakfastItemsList, breakfastMenuName)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error
                                Log.e("MenuReceiver", "Error fetching breakfast menu items", error.toException())
                                callback.onError(error.message)
                            }
                        })
                    } else {
                        callback.onError("Restaurant not found")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e("MenuReceiver", "Error fetching restaurant data", error.toException())
                    callback.onError(error.message)
                }
            })


}
    fun getMenuNamesList(): List<String> {
        return menuNamesList
    }

}
