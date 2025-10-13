package com.example.pal_rohit_tilva_stressmeter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
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
            for (line in lines) {
                val parts = line.split(",")
                if (parts.size == 2) {
                    val timestamp = parts[0]
                    val score = parts[1].toIntOrNull() ?: continue
                    scores.add(score)

                    // Add row to table
                    val row = TableRow(requireContext())
                    val timeText = TextView(requireContext())
                    val scoreText = TextView(requireContext())
                    timeText.text = timestamp
                    scoreText.text = score.toString()
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
