package com.example.myapplication

import ImageAdapter
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import java.util.Locale

class Dashboard : AppCompatActivity() {


    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: ImageAdapter
    private lateinit var timer: CountDownTimer

    private lateinit var test2: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)
        // Inside your MainActivity.kt or any other relevant activity or class


        test2=findViewById(R.id.profile)


        test2.setOnClickListener {
            val intent = Intent(this, FavouritesActivity::class.java)
            startActivity(intent)
        }


        val text = findViewById<TextView>(R.id.restaurant_name)
        viewPager = findViewById(R.id.imageSlider)

        // Initialize ImageAdapter
        adapter = ImageAdapter(this)
        viewPager.adapter = adapter

        // Start the countdown timer
        startTimer()

        searchEditText = findViewById(R.id.search_bar)
        searchButton = findViewById(R.id.searchBtn)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                val lowercaseQuery = query.toLowerCase(Locale.ROOT) // Convert query to lowercase
                val intent = Intent(this, ResultsActivity::class.java)
                intent.putExtra("query", lowercaseQuery)
                startActivity(intent)
            }
        }
    }

    private fun startTimer() {
        // Start a countdown timer to change ViewPager's current item every 3 seconds
        timer = object : CountDownTimer(Long.MAX_VALUE, 3000) {
            override fun onTick(millisUntilFinished: Long) {
                if (adapter.count > 0) {
                    val currentItem = viewPager.currentItem
                    viewPager.currentItem = (currentItem + 1) % adapter.count
                }
            }

            override fun onFinish() {
                // Timer finished
            }
        }
        timer.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        // Cancel the timer when the activity is destroyed to avoid memory leaks
        timer.cancel()
    }

}
