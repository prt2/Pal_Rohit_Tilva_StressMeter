package com.example.pal_rohit_tilva_stressmeter

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class ImagePreviewActivity : AppCompatActivity() {

    /**
     * Initializes the activity, sets up the views, and handles the user's actions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val imageView = findViewById<ImageView>(R.id.previewImageView)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        // Get the image resource ID and the stress score from the intent
        val imageResId = intent.getIntExtra("imageResId", 0)
        val score = intent.getIntExtra("score", 0)
        imageView.setImageResource(imageResId)

        // Set a click listener for the "Submit" button
        btnSubmit.setOnClickListener {
            val timestamp = System.currentTimeMillis() / 1000
            val file = File(filesDir, "stress_timestamp.csv")

            // Append the timestamp and score to the CSV file
            BufferedWriter(FileWriter(file, true)).use { writer ->
                writer.append("$timestamp,$score\n")
            }

            // Close the app
            finishAffinity()
        }

        // Set a click listener for the "Cancel" button
        btnCancel.setOnClickListener {
            // Go back to the previous activity
            finish()
        }
    }
}
