package com.example.testapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// Google Maps API Key: AIzaSyCWKnwIeuF-W3_kUO2jRSD6iYd2nh_SdQo

class Googlemaps : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_googlemaps)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "New Activity"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
