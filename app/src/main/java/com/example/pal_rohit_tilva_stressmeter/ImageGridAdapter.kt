package com.example.pal_rohit_tilva_stressmeter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageGridAdapter(private val context: Context, private var imageList: List<Int>) : BaseAdapter() {
    override fun getCount() = imageList.size
    override fun getItem(position: Int) = imageList[position]
    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = convertView as? ImageView ?: ImageView(context)
        imageView.layoutParams = ViewGroup.LayoutParams(250, 250)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageResource(imageList[position])
        return imageView
    }

    private val stressScores = listOf(
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
    )

    fun updateData(newList: List<Int>) {
        imageList = newList
        notifyDataSetChanged()
    }

    fun getScore(position: Int): Int {
        return stressScores.getOrElse(position) { 0 }
    }

}
