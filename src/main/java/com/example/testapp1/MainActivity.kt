package com.example.testapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Nav button to Geo
        val button = findViewById<Button>(R.id.button)
            button.setOnClickListener {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }

        // Nav button to Drawer
        val drawerbutton = findViewById<Button>(R.id.drawerbutton)
            drawerbutton.setOnClickListener {
            val intent = Intent(this, Main2ActivityDrawer::class.java)
            startActivity(intent)
        }

        // Main Function (Button roller)
        val rollButton = findViewById<Button>(R.id.rollButton)
        val resultsTextView = findViewById<TextView>(R.id.ResultsTextView)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        rollButton.setOnClickListener {
            val rand = Random.nextInt(seekBar.progress) + 1
            resultsTextView.text = rand.toString()
        }
    }
}
