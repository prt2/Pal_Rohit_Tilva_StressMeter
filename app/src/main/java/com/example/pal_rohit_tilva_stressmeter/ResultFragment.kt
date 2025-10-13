package com.example.pal_rohit_tilva_stressmeter

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import java.io.File

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)
        val tableLayout = view.findViewById<TableLayout>(R.id.resultsTable)
        val graphView = view.findViewById<GraphView>(R.id.graphView)

        val file = File(requireContext().filesDir, "stress_timestamp.csv")
        val scores = mutableListOf<Int>()

        if (file.exists()) {
            val lines = file.readLines()

            lines.forEachIndexed { index, line ->
                val parts = line.split(",")
                if (parts.size == 2) {
                    val timestamp = parts[0]
                    val score = parts[1].toIntOrNull() ?: return@forEachIndexed
                    scores.add(score)

                    val row = TableRow(requireContext()).apply {
                        if (index % 2 == 0) setBackgroundColor(Color.parseColor("#F7F7F7"))
                        else setBackgroundColor(Color.parseColor("#FFFFFF"))
                    }

                    val border = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(1, 1, 1, 1)
                    }

                    val timeText = TextView(requireContext()).apply {
                        text = timestamp
                        textSize = 15f
                        setTextColor(Color.BLACK)
                        setPadding(8, 6, 8, 6)
                        layoutParams = border
                    }

                    val scoreText = TextView(requireContext()).apply {
                        text = score.toString()
                        textSize = 15f
                        gravity = Gravity.CENTER
                        setTypeface(null, Typeface.BOLD)
                        setTextColor(Color.BLACK)
                        setPadding(8, 6, 8, 6)
                        layoutParams = border
                    }

                    row.addView(timeText)
                    row.addView(scoreText)
                    tableLayout.addView(row)
                }
            }
        }

        graphView.scores = scores
        graphView.invalidate()
        return view
    }
}
