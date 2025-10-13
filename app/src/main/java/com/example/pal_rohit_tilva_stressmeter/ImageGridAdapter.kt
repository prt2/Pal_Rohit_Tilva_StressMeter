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
        imageView.setPadding(8, 8, 8, 8)
        imageView.setImageResource(imageList[position])
        return imageView
    }

    fun updateData(newList: List<Int>) {
        imageList = newList
        notifyDataSetChanged()
    }
}
