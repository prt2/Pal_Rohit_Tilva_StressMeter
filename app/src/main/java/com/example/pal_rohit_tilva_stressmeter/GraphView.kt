package com.example.pal_rohit_tilva_stressmeter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

class GraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var scores: List<Int> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val axisPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 2f
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
        color = Color.argb(100, 0, 0, 255)
        style = Paint.Style.FILL
    }

    private val pointPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }

    private val textPaint = Paint().apply {
        color = Color.DKGRAY
        textSize = 28f
        isAntiAlias = true
    }

    private val labelPaint = Paint().apply {
        color = Color.GRAY
        textSize = 30f
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    // Zoom variables
    private var scaleFactor = 1f
    private val minZoom = 1f
    private val maxZoom = 3f
    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            scaleFactor = if (scaleFactor > 1f) 1f else 2f
            invalidate()
            return true
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (scores.isEmpty()) return

        val padding = 100f
        val graphWidth = (width - 2 * padding) * scaleFactor
        val graphHeight = height - 2 * padding

        // origin
        val originX = padding
        val originY = height - padding

        // axes
        canvas.drawLine(originX, padding, originX, originY, axisPaint)
        canvas.drawLine(originX, originY, width - padding, originY, axisPaint)

        // labels
        canvas.drawText("Stress Level", 50f, padding - 20f, labelPaint)
        canvas.drawText("Instances", width / 2f, height - 30f, labelPaint)

        val maxY = max(scores.maxOrNull()?.toFloat() ?: 10f, 10f)
        val stepX = graphWidth / (scores.size - 1).coerceAtLeast(1)

        // grid + y labels
        for (i in 0..10) {
            val y = originY - (graphHeight / 10) * i
            canvas.drawLine(originX, y, width - padding, y, gridPaint)
            canvas.drawText("${(maxY / 10 * i).toInt()}", 20f, y + 10f, textPaint)
        }

        val path = Path()
        val fillPath = Path().apply { moveTo(originX, originY) }

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

            canvas.drawCircle(x, y, 8f, pointPaint)
        }

        fillPath.lineTo(originX + (scores.size - 1) * stepX, originY)
        fillPath.close()

        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(path, linePaint)
    }
}
