package com.example.pal_rohit_tilva_stressmeter

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class ImagePreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val imageView = findViewById<ImageView>(R.id.previewImageView)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val imageResId = intent.getIntExtra("imageResId", 0)
        val score = intent.getIntExtra("score", 0)
        imageView.setImageResource(imageResId)

        btnSubmit.setOnClickListener {
            val timestamp = System.currentTimeMillis() / 1000
            val file = File(filesDir, "stress_timestamp.csv")

            BufferedWriter(FileWriter(file, true)).use { writer ->
                writer.append("$timestamp,$score\n")
            }

            // Exit the app (as per assignment)
            finishAffinity()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}
