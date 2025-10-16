package com.example.pal_rohit_tilva_stressmeter

import com.anychart.chart.common.dataentry.DataEntry
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import java.io.File

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val tableLayout = view.findViewById<TableLayout>(R.id.resultsTable)
        val anyChartView = view.findViewById<AnyChartView>(R.id.any_chart_view)

        val file = File(requireContext().filesDir, "stress_timestamp.csv")
        val scores = mutableListOf<Int>()
        val timestamps = mutableListOf<String>()

        // 🧾 Fill table and collect data
        if (file.exists()) {
            val lines = file.readLines()
            lines.forEachIndexed { index, line ->
                val parts = line.split(",")
                if (parts.size == 2) {
                    val timestamp = parts[0]
                    val score = parts[1].toIntOrNull() ?: return@forEachIndexed
                    scores.add(score)
                    timestamps.add(timestamp)

                    val row = TableRow(requireContext()).apply {
                        if (index % 2 == 0) setBackgroundColor(Color.parseColor("#F7F7F7"))
                        else setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }

                    val cellParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    ).apply { setMargins(1, 1, 1, 1) }

                    val timeText = TextView(requireContext()).apply {
                        text = timestamp
                        textSize = 15f
                        setTextColor(Color.BLACK)
                        setPadding(8, 6, 8, 6)
                        layoutParams = cellParams
                    }

                    val scoreText = TextView(requireContext()).apply {
                        text = score.toString()
                        textSize = 15f
                        gravity = Gravity.CENTER
                        setTypeface(null, Typeface.BOLD)
                        setTextColor(Color.BLACK)
                        setPadding(8, 6, 8, 6)
                        layoutParams = cellParams
                    }

                    row.addView(timeText)
                    row.addView(scoreText)
                    tableLayout.addView(row)
                }
            }
        }

        // 📊 Build the AnyChart line chart
        val lineChart = AnyChart.line()
        lineChart.animation(true)
        lineChart.title("Stress Levels Over Time")
        lineChart.background().fill("#FFFFFF")

        // ✅ Use instance numbers (1, 2, 3, …) instead of timestamps
        val seriesData: MutableList<DataEntry> = mutableListOf()
        for (i in scores.indices) {
            seriesData.add(ValueDataEntry(i + 1, scores[i])) // instance number instead of timestamp
        }

        val set = com.anychart.data.Set.instantiate()
        set.data(seriesData)
        val mapping = set.mapAs("{ x: 'x', value: 'value' }")

        val series = lineChart.line(mapping)
        series.name("Stress Score")
        series.hovered().markers().enabled(true)
        series.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4)
        series.tooltip()
            .positionMode(TooltipPositionMode.POINT)
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5)
            .offsetY(5)

        lineChart.yAxis(0).title("Score")
        lineChart.xAxis(0).title("Instances") // ← changed label

        anyChartView.setChart(lineChart)

        return view
    }
}
