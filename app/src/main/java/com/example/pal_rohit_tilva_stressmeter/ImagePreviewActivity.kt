package com.example.pal_rohit_tilva_stressmeter

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImagePreviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        val cancelButton = findViewById<Button>(R.id.cancelButton)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val previewImage = findViewById<ImageView>(R.id.previewImageView)

        val imageResId = intent.getIntExtra("imageResId", 0)
        previewImage.setImageResource(imageResId)

        cancelButton.setOnClickListener { finish() }

        submitButton.setOnClickListener {
            finish() // later could save stress level entry here
        }
    }
}
