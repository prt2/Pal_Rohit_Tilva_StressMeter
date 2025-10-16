package com.example.pal_rohit_tilva_stressmeter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.fragment.app.Fragment


class StressMeterFragment : Fragment() {

    // The GridView that displays the images
    private lateinit var imageGrid: GridView
    // The adapter for the GridView
    private lateinit var imageGridAdapter: ImageGridAdapter

    // A list of all the image resources
    private var allImages: List<Int> = emptyList()
    // The current page of images being displayed
    private var currentPage = 0
    // The number of images to display per page
    private val imagesPerPage = 16

    /**
     * Inflates the layout for this fragment, initializes views, and sets up the image grid.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stress_meter, container, false)

        imageGrid = view.findViewById(R.id.imageGrid)
        val moreButton = view.findViewById<Button>(R.id.moreImagesButton)

        // Dynamically load all drawables that start with "psm_"
        allImages = getAllPsmDrawables()

        // Update the grid with the first page of images
        updateGrid()

        // Set a click listener for the items in the grid
        imageGrid.setOnItemClickListener { _, _, position, _ ->
            val globalIndex = currentPage * imagesPerPage + position
            if (globalIndex < allImages.size) {
                val imageResId = allImages[globalIndex]

                // The stress scores are based on the position of the image in the grid
                val stressScores = listOf(
                    6, 8, 14, 16,  // Row 1
                    5, 7, 13, 15,  // Row 2
                    2, 4, 10, 12,  // Row 3
                    1, 3, 9, 11    // Row 4
                )
                val score = stressScores[globalIndex]

                // Start the ImagePreviewActivity to show the selected image and its score
                val intent = Intent(requireContext(), ImagePreviewActivity::class.java).apply {
                    putExtra("imageResId", imageResId)
                    putExtra("score", score)
                }
                startActivity(intent)
            }
        }

        // Set a click listener for the "More Images" button
        moreButton.setOnClickListener {
            currentPage = (currentPage + 1) % ((allImages.size + imagesPerPage - 1) / imagesPerPage)
            updateGrid()
        }

        return view
    }

    /**
     * Updates the grid with the current page of images.
     */
    private fun updateGrid() {
        if (allImages.isEmpty()) return
        val startIndex = currentPage * imagesPerPage
        val endIndex = minOf(startIndex + imagesPerPage, allImages.size)
        val currentImages = allImages.subList(startIndex, endIndex)

        imageGridAdapter = ImageGridAdapter(requireContext(), currentImages)
        imageGrid.adapter = imageGridAdapter
    }

    /**
     * Gets a list of all drawable resources that start with "psm_".
     */
    private fun getAllPsmDrawables(): List<Int> {
        val fields = R.drawable::class.java.fields
        return fields
            .filter { it.name.startsWith("psm_") } // match all "psm_" drawables
            .mapNotNull {
                try {
                    it.getInt(null)
                } catch (e: Exception) {
                    null
                }
            }
    }
}
