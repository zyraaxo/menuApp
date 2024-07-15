import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.myapplication.R
import com.squareup.picasso.Picasso

class ImageAdapter(private val context: Context) : PagerAdapter() {

    private val firebaseDatabaseHelper = FirebaseDatabaseHelper()
    private var restaurantList: List<FirebaseDatabaseHelper.Restaurant> = emptyList()
    private var restaurantNames: List<String> = emptyList()

    private val handler = Handler(Looper.getMainLooper())
    private val slideRunnable = object : Runnable {
        override fun run() {
            val nextItem = if (currentPosition == getCount() - 1) 0 else currentPosition + 1
            viewPager.currentItem = nextItem
            handler.postDelayed(this, 3000) // Slide every 3 seconds
        }
    }

    private lateinit var viewPager: androidx.viewpager.widget.ViewPager
    private var currentPosition = 0

    init {
        fetchRestaurants()
    }

    private fun fetchRestaurants() {
        firebaseDatabaseHelper.fetchRestaurantsFromDatabase { restaurants ->
            restaurantList = restaurants
            restaurantNames = restaurants.map { it.name }
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return minOf(4, restaurantList.size)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_image, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val restaurantNameTextView = view.findViewById<TextView>(R.id.restaurant_names)

        val restaurant = restaurantList[position]
        Picasso.get().load(restaurant.imageURL).into(imageView)
        restaurantNameTextView.text = restaurant.name

        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    fun setViewPager(viewPager: androidx.viewpager.widget.ViewPager) {
        this.viewPager = viewPager
    }

    fun startTimer() {
        handler.postDelayed(slideRunnable, 3000) // Start the timer with a delay of 3 seconds
    }

    fun stopTimer() {
        handler.removeCallbacks(slideRunnable) // Stop the timer
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, obj: Any) {
        super.setPrimaryItem(container, position, obj)
        currentPosition = position
    }

    fun getRestaurantNames(): List<String> {
        return restaurantNames
    }
}
