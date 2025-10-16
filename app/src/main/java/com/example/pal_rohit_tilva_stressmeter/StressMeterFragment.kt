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

    private lateinit var imageGrid: GridView
    private lateinit var imageGridAdapter: ImageGridAdapter

    private var allImages: List<Int> = emptyList()
    private var currentPage = 0
    private val imagesPerPage = 16

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stress_meter, container, false)

        imageGrid = view.findViewById(R.id.imageGrid)
        val moreButton = view.findViewById<Button>(R.id.moreImagesButton)

        // Dynamically load all drawables that start with "psm_"
        allImages = getAllPsmDrawables()

        updateGrid()

        imageGrid.setOnItemClickListener { _, _, position, _ ->
            val globalIndex = currentPage * imagesPerPage + position
            if (globalIndex < allImages.size) {
                val imageResId = allImages[globalIndex]

                // ✅ Stress scores based on your reference grid (1–16 pattern)
                val stressScores = listOf(
                    6, 8, 14, 16,  // Row 1
                    5, 7, 13, 15,  // Row 2
                    2, 4, 10, 12,  // Row 3
                    1, 3, 9, 11    // Row 4
                )
                val score = stressScores[globalIndex]


                val intent = Intent(requireContext(), ImagePreviewActivity::class.java).apply {
                    putExtra("imageResId", imageResId)
                    putExtra("score", score)
                }
                startActivity(intent)
            }
        }

        moreButton.setOnClickListener {
            currentPage = (currentPage + 1) % ((allImages.size + imagesPerPage - 1) / imagesPerPage)
            updateGrid()
        }

        return view
    }

    private fun updateGrid() {
        if (allImages.isEmpty()) return
        val startIndex = currentPage * imagesPerPage
        val endIndex = minOf(startIndex + imagesPerPage, allImages.size)
        val currentImages = allImages.subList(startIndex, endIndex)

        imageGridAdapter = ImageGridAdapter(requireContext(), currentImages)
        imageGrid.adapter = imageGridAdapter
    }

    // ✅ Automatically collect all drawables named psm_*
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
            .sortedBy { it } // optional: keeps list order consistent
    }
}
