package com.example.pal_rohit_tilva_stressmeter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class StressGraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var stressData: List<Int> = emptyList()
    private val linePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val fillPaint = Paint().apply {
        color = Color.parseColor("#80339FFF") // translucent blue fill
        style = Paint.Style.FILL
    }
    private val dotPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    fun setStressData(data: List<Int>) {
        stressData = data
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (stressData.isEmpty()) return

        val maxY = (stressData.maxOrNull() ?: 1).toFloat()
        val stepX = width.toFloat() / (stressData.size - 1)
        val scaleY = height / maxY

        val points = stressData.mapIndexed { index, value ->
            val x = index * stepX
            val y = height - (value * scaleY)
            x to y
        }

        // Fill under curve
        val fillPath = android.graphics.Path().apply {
            moveTo(0f, height.toFloat())
            for ((x, y) in points) lineTo(x, y)
            lineTo(width.toFloat(), height.toFloat())
            close()
        }
        canvas.drawPath(fillPath, fillPaint)

        // Draw line and dots
        for (i in 0 until points.size - 1) {
            val (x1, y1) = points[i]
            val (x2, y2) = points[i + 1]
            canvas.drawLine(x1, y1, x2, y2, linePaint)
        }
        for ((x, y) in points) canvas.drawCircle(x, y, 8f, dotPaint)
    }
}
