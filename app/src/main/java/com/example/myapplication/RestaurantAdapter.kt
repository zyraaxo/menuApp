import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.example.myapplication.FavoriteManager
import com.example.myapplication.R
import com.example.myapplication.RestaurantActivity
import com.squareup.picasso.Picasso
import android.widget.Toast


class RestaurantAdapter(private val context: Context, private var dataSource: List<FirebaseDatabaseHelper.Restaurant>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = inflater.inflate(R.layout.list_item_restaurant, parent, false)
            holder = ViewHolder()
            holder.titleTextView = view.findViewById(R.id.restaurantName)
            holder.thumbnailImageView = view.findViewById(R.id.restaurantImage)
            holder.favoriteButton = view.findViewById(R.id.favoriteToggleButton)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val restaurant = getItem(position) as FirebaseDatabaseHelper.Restaurant
        holder.titleTextView.text = restaurant.name

        // Set the size and margin of the image
        val imageViewParams = holder.thumbnailImageView.layoutParams as ViewGroup.MarginLayoutParams
        imageViewParams.width = 1400  // desired width in pixels
        imageViewParams.height = 400 // desired height in pixels
        imageViewParams.leftMargin = 16 // desired left margin in pixels
        holder.thumbnailImageView.layoutParams = imageViewParams

        // Apply the rounded transformation
        val radius = 30f // desired corner radius
        Picasso.get()
            .load(restaurant.imageURL)
            .resize(1400, 400)
            .transform(RoundedTransformation(radius))
            .into(holder.thumbnailImageView)

        holder.favoriteButton.isChecked = FavoriteManager.isFavorite(restaurant.name)
        holder.favoriteButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                FavoriteManager.addFavorite(restaurant.name)
                showToast(context, "Added to favorites!")
            } else {
                FavoriteManager.removeFavorite(restaurant.name)
            }
        }

        view!!.setOnClickListener {
            val intent = Intent(context, RestaurantActivity::class.java)
            intent.putExtra("restaurantName", restaurant.name)
            intent.putExtra("restaurantImage", restaurant.imageURL)
            context.startActivity(intent)
        }

        return view!!
    }


    fun updateData(newData: List<FirebaseDatabaseHelper.Restaurant>) {
        dataSource = newData
        notifyDataSetChanged()
    }

    private class ViewHolder {
        lateinit var thumbnailImageView: ImageView
        lateinit var titleTextView: TextView
        lateinit var favoriteButton: ToggleButton
    }
    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}
