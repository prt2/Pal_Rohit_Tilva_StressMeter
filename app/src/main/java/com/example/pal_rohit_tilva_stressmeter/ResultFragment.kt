package com.example.pal_rohit_tilva_stressmeter

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import java.io.File


class ResultFragment : Fragment() {

    /**
     * Inflates the layout for this fragment, initializes views, and triggers the loading of data.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val tableLayout = view.findViewById<TableLayout>(R.id.resultsTable)
        val anyChartView = view.findViewById<AnyChartView>(R.id.any_chart_view)

        // Check for media permissions before loading data
        if (hasMediaPermission()) {
            loadGraphAndTable(tableLayout, anyChartView)
        } else {
            requestMediaPermission()
        }

        return view
    }

    /**
     * Checks if the app has the necessary permissions to read media files.
     * The required permission depends on the Android version.
     */
    private fun hasMediaPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, check for specific media permissions
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_MEDIA_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For older versions, check for external storage permission
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    /**
     * Requests the necessary media permissions from the user.
     */
    private fun requestMediaPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
                ),
                300
            )
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 300)
        }
    }

    /**
     * Handles the result of the permission request.
     * If permissions are granted, it loads the graph and table.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 300 && grantResults.isNotEmpty() &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            val tableLayout = view?.findViewById<TableLayout>(R.id.resultsTable)
            val anyChartView = view?.findViewById<AnyChartView>(R.id.any_chart_view)
            if (tableLayout != null && anyChartView != null) {
                loadGraphAndTable(tableLayout, anyChartView)
            }
        }
    }

    /**
     * Loads stress data from a CSV file, populates a table with the data,
     * and generates a line chart to visualize the stress levels over time.
     */
    private fun loadGraphAndTable(
        tableLayout: TableLayout,
        anyChartView: AnyChartView
    ) {
        val file = File(requireContext().filesDir, "stress_timestamp.csv")
        val scores = mutableListOf<Int>()
        val timestamps = mutableListOf<String>()

        // If the data file doesn't exist, show a message and return
        if (!file.exists()) {
            val info = TextView(requireContext()).apply {
                text = "No stress data recorded yet."
                textSize = 16f
                setTextColor(Color.GRAY)
                gravity = Gravity.CENTER
                setPadding(16, 80, 16, 16)
            }
            tableLayout.addView(info)
            return
        }

        // Read the CSV file line by line
        val lines = file.readLines()
        lines.forEachIndexed { index, line ->
            val parts = line.split(",")
            if (parts.size == 2) {
                val timestamp = parts[0]
                val score = parts[1].toIntOrNull() ?: return@forEachIndexed
                scores.add(score)
                timestamps.add(timestamp)

                // Create a new row for the table
                val row = TableRow(requireContext()).apply {
                    if (index % 2 == 0)
                        setBackgroundColor(Color.parseColor("#F7F7F7"))
                    else
                        setBackgroundColor(Color.parseColor("#FFFFFF"))
                }

                val cellParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(1, 1, 1, 1) }

                // Create a TextView for the timestamp
                val timeText = TextView(requireContext()).apply {
                    text = timestamp
                    textSize = 15f
                    setTextColor(Color.BLACK)
                    setPadding(8, 6, 8, 6)
                    layoutParams = cellParams
                }

                // Create a TextView for the stress score
                val scoreText = TextView(requireContext()).apply {
                    text = score.toString()
                    textSize = 15f
                    gravity = Gravity.CENTER
                    setTypeface(null, Typeface.BOLD)
                    setTextColor(Color.BLACK)
                    setPadding(8, 6, 8, 6)
                    layoutParams = cellParams
                }

                // Add the TextViews to the row and the row to the table
                row.addView(timeText)
                row.addView(scoreText)
                tableLayout.addView(row)
            }
        }

        // Create and configure the line chart
        val lineChart = AnyChart.line()
        lineChart.animation(true)
        lineChart.title("Stress Levels Over Time")
        lineChart.background().fill("#FFFFFF")

        // Prepare the data for the chart
        val seriesData: MutableList<DataEntry> = mutableListOf()
        for (i in timestamps.indices) {
            seriesData.add(ValueDataEntry("${i + 1}", scores[i]))
        }

        val set = com.anychart.data.Set.instantiate()
        set.data(seriesData)
        val mapping = set.mapAs("{ x: 'x', value: 'value' }")

        // Create the line series and configure its appearance
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

        // Set the titles for the axes
        lineChart.yAxis(0).title("Score")
        lineChart.xAxis(0).title("Instances")

        // Set the chart to the view
        anyChartView.setChart(lineChart)
    }
}
