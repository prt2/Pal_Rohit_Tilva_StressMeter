package com.example.pal_rohit_tilva_stressmeter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageGridAdapter(private val context: Context, private var imageList: List<Int>) : BaseAdapter() {
    /**
     * Returns the number of items in the data set represented by this Adapter.
     */
    override fun getCount() = imageList.size

    /**
     * Get the data item associated with the specified position in the data set.
     */
    override fun getItem(position: Int) = imageList[position]

    /**
     * Get the row id associated with the specified position in the list.
     */
    override fun getItemId(position: Int) = position.toLong()

    /**
     * Get a View that displays the data at the specified position in the data set.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // If convertView is null, create a new ImageView
        val imageView = convertView as? ImageView ?: ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(250, 250)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(imageList[position])
        return imageView
    }

    // The stress scores are based on the position of the image in the grid
    private val stressScores = listOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
    )

    /**
     * Updates the data in the adapter and notifies the GridView to refresh.
     */
    fun updateData(newList: List<Int>) {
        imageList = newList
        notifyDataSetChanged()
    }

    /**
     * Returns the stress score for the image at the specified position.
     */
    fun getScore(position: Int): Int {
        return stressScores.getOrElse(position) { 0 }
    }

}
