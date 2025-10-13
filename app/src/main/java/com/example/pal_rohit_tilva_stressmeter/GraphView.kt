package com.example.pal_rohit_tilva_stressmeter

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class GraphView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var scores: List<Int> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private var minX = 0f
    private var maxX = 0f
    private var zoomFactor = 1f
    private var lastTapTime = 0L

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
        strokeWidth = 6f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        color = Color.argb(100, 0, 0, 255)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val pointPaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 30f
        isAntiAlias = true
    }

    init {
        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val now = System.currentTimeMillis()
                if (now - lastTapTime < 300) { // double-tap detected
                    handleDoubleTap(event.x)
                }
                lastTapTime = now
            }
            true
        }
    }

    private fun handleDoubleTap(x: Float) {
        if (scores.isEmpty()) return

        val center = minX + (x / width) * (maxX - minX)
        if (zoomFactor < 4f) zoomFactor *= 2f else zoomFactor = 1f

        val visibleCount = (scores.size - 1) / zoomFactor
        minX = (center - visibleCount / 2f).coerceAtLeast(0f)
        maxX = (center + visibleCount / 2f).coerceAtMost((scores.size - 1).toFloat())
        if (maxX - minX < 1f) maxX = (minX + 1f).coerceAtMost((scores.size - 1).toFloat())

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (scores.isEmpty()) return

        val paddingLeft = 100f
        val paddingRight = 60f
        val paddingTop = 80f
        val paddingBottom = 100f
        val graphWidth = width - paddingLeft - paddingRight
        val graphHeight = height - paddingTop - paddingBottom
        val originX = paddingLeft
        val originY = height - paddingBottom

        if (maxX == 0f) maxX = (scores.size - 1).toFloat()
        val maxY = max(scores.maxOrNull()?.toFloat() ?: 10f, 10f)

        // Draw Y-axis grid and labels
        for (i in 0..10) {
            val y = originY - (graphHeight / 10) * i
            canvas.drawLine(originX, y, width - paddingRight, y, gridPaint)
            val label = ((maxY / 10) * i).roundToInt().toString()
            canvas.drawText(label, originX - 70f, y + 10f, textPaint)
        }

        val visibleCount = maxX - minX
        val stepX = graphWidth / visibleCount

        // --- Interpolated curve for smoothness ---
        val smoothPoints = mutableListOf<Pair<Float, Float>>()
        val startIndex = (minX - 1).coerceAtLeast(0f)
        val endIndex = (maxX + 1).coerceAtMost((scores.size - 1).toFloat())

        var i = startIndex
        while (i <= endIndex) {
            val left = i.toInt()
            val right = min(left + 1, scores.lastIndex)
            val t = i - left
            val interpolated = scores[left] * (1 - t) + scores[right] * t
            val normX = (i - minX)
            val x = originX + normX * stepX
            val y = originY - (interpolated / maxY) * graphHeight
            smoothPoints.add(x to y)
            i += 0.1f
        }

        if (smoothPoints.isEmpty()) return

        // Fill under the curve
        val fillPath = Path().apply {
            moveTo(smoothPoints.first().first, originY)
            for ((x, y) in smoothPoints) lineTo(x, y)
            lineTo(smoothPoints.last().first, originY)
            close()
        }
        canvas.drawPath(fillPath, fillPaint)

        // Draw smooth line
        val linePath = Path()
        linePath.moveTo(smoothPoints.first().first, smoothPoints.first().second)
        for ((x, y) in smoothPoints) linePath.lineTo(x, y)
        canvas.drawPath(linePath, linePaint)

        // Draw actual data points only (not every step)
        for (index in scores.indices) {
            if (index < minX || index > maxX) continue
            val x = originX + (index - minX) * stepX
            val y = originY - (scores[index] / maxY) * graphHeight
            canvas.drawCircle(x, y, 10f, pointPaint)
        }

        // Axes
        canvas.drawLine(originX, paddingTop, originX, originY, axisPaint)
        canvas.drawLine(originX, originY, width - paddingRight, originY, axisPaint)

        // X-axis ticks with decimals
        val tickCount = 6
        for (iTick in 0..tickCount) {
            val fraction = iTick / tickCount.toFloat()
            val dataValue = minX + fraction * visibleCount
            val x = originX + fraction * graphWidth
            val label = String.format("%.1f", dataValue)
            canvas.drawText(label, x - 15f, originY + 40f, textPaint)
        }

        // Axis titles
        canvas.drawText("Instances", width / 2f - 60f, height - 25f, textPaint)
        canvas.save()
        canvas.rotate(-90f, 0f, 0f)
        canvas.drawText("Stress Level", -height / 2f - 40f, 45f, textPaint)
        canvas.restore()
    }
}
