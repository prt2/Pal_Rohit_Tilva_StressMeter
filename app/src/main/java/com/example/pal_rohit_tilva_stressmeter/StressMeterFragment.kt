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
    private lateinit var moreButton: Button

    // 🔹 All 49 drawable images you provided
    private val allImages = listOf(
        R.drawable.fish_normal017, R.drawable.psm_alarm_clock, R.drawable.psm_alarm_clock2,
        R.drawable.psm_angry_face, R.drawable.psm_anxious, R.drawable.psm_baby_sleeping,
        R.drawable.psm_bar, R.drawable.psm_barbed_wire2, R.drawable.psm_beach3,
        R.drawable.psm_bird3, R.drawable.psm_blue_drop, R.drawable.psm_cat,
        R.drawable.psm_clutter, R.drawable.psm_clutter3, R.drawable.psm_dog_sleeping,
        R.drawable.psm_exam4, R.drawable.psm_gambling4, R.drawable.psm_headache,
        R.drawable.psm_headache2, R.drawable.psm_hiking3, R.drawable.psm_kettle,
        R.drawable.psm_lake3, R.drawable.psm_lawn_chairs3, R.drawable.psm_lonely,
        R.drawable.psm_lonely2, R.drawable.psm_mountains11, R.drawable.psm_neutral_child,
        R.drawable.psm_neutral_person2, R.drawable.psm_peaceful_person, R.drawable.psm_puppy,
        R.drawable.psm_puppy3, R.drawable.psm_reading_in_bed2, R.drawable.psm_running3,
        R.drawable.psm_running4, R.drawable.psm_sticky_notes2, R.drawable.psm_stressed_cat,
        R.drawable.psm_stressed_person, R.drawable.psm_stressed_person3,
        R.drawable.psm_stressed_person4, R.drawable.psm_stressed_person6,
        R.drawable.psm_stressed_person7, R.drawable.psm_stressed_person12,
        R.drawable.psm_talking_on_phone2, R.drawable.psm_to_do_list,
        R.drawable.psm_to_do_list3, R.drawable.psm_wine3, R.drawable.psm_work4,
        R.drawable.psm_yoga4
    )

    private var currentSetIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stress_meter, container, false)

        imageGrid = view.findViewById(R.id.imageGrid)
        moreButton = view.findViewById(R.id.moreButton)

        // show the first 16 images
        showImageSet(0)

        imageGrid.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), ImagePreviewActivity::class.java)
            intent.putExtra("imageResId", imageGridAdapter.getItem(position) as Int)
            startActivity(intent)
        }

        moreButton.setOnClickListener {
            currentSetIndex = (currentSetIndex + 1) % getTotalSets()
            showImageSet(currentSetIndex)
        }

        return view
    }

    private fun getTotalSets(): Int = (allImages.size + 15) / 16

    private fun showImageSet(index: Int) {
        val start = index * 16
        val end = minOf(start + 16, allImages.size)
        val currentSet = allImages.subList(start, end)
        imageGridAdapter = ImageGridAdapter(requireContext(), currentSet)
        imageGrid.adapter = imageGridAdapter
    }
}
