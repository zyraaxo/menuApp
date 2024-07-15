import android.util.Log
import com.google.firebase.database.*

class FirebaseDatabaseHelper {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("restaurants")

    // Data model for Restaurant
    data class Restaurant(
        val name: String = "",
        val imageURL: String = ""
    )

    // Function to fetch restaurants from Realtime Database
    fun fetchRestaurantsFromDatabase(completion: (List<Restaurant>) -> Unit) {
        val restaurantList = mutableListOf<Restaurant>()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (childSnapshot in snapshot.children) {
                    val name = childSnapshot.child("name").getValue(String::class.java) ?: ""
                    val imageURL = childSnapshot.child("imageURL").getValue(String::class.java) ?: ""
                    val restaurant = Restaurant(name, imageURL)
                    restaurantList.add(restaurant)
                }
                completion(restaurantList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("FirebaseDatabaseHelper", "Error fetching restaurants", error.toException())
                completion(emptyList())
            }
        })
    }
}
