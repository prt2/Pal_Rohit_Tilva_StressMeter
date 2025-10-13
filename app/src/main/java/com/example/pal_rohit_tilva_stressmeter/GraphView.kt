package com.example.pal_rohit_tilva_stressmeter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.roundToInt

class GraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var scores: List<Int> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val gridPaint = Paint().apply {
        color = Color.LTGRAY
        strokeWidth = 1f
        style = Paint.Style.STROKE
    }

    private val linePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        color = Color.argb(80, 0, 0, 255) // light blue shade
        style = Paint.Style.FILL
    }

    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (scores.isEmpty()) return

        val padding = 100f
        val graphWidth = width - 2 * padding
        val graphHeight = height - 2 * padding

        // Draw axes
        val originX = padding
        val originY = height - padding
        canvas.drawLine(originX, padding, originX, originY, axisPaint)
        canvas.drawLine(originX, originY, width - padding, originY, axisPaint)

        val maxY = (scores.maxOrNull() ?: 10).toFloat().coerceAtLeast(10f)
        val stepX = graphWidth / (scores.size - 1).coerceAtLeast(1)

        // Grid lines + labels
        for (i in 0..10) {
            val y = originY - (graphHeight / 10) * i
            canvas.drawLine(originX, y, width - padding, y, gridPaint)
            canvas.drawText("${(maxY / 10 * i).roundToInt()}", 10f, y + 10f, textPaint)
        }

        // Build path for line & fill
        val path = Path()
        val fillPath = Path()
        fillPath.moveTo(originX, originY)

        scores.forEachIndexed { i, score ->
            val x = originX + i * stepX
            val y = originY - (score / maxY) * graphHeight

            if (i == 0) {
                path.moveTo(x, y)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            // Draw data point
            canvas.drawCircle(x, y, 8f, pointPaint)
        }

        fillPath.lineTo(originX + (scores.size - 1) * stepX, originY)
        fillPath.close()

        // Fill + draw line
        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(path, linePaint)
    }
}
