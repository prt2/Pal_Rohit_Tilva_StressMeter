package com.example.pal_rohit_tilva_stressmeter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val chartView = view.findViewById<StressGraphView>(R.id.stressGraph)
        val table = view.findViewById<TableLayout>(R.id.resultsTable)

        // --- Fake data for now ---
        val stressValues = listOf(7, 10, 7, 2)
        val timestamps = listOf("1760314705", "1760314739", "1760314749", "1760314768")

        chartView.setStressData(stressValues)

        // --- Header row ---
        val header = TableRow(requireContext())
        val timeHeader = TextView(requireContext())
        val stressHeader = TextView(requireContext())
        timeHeader.text = "Time"
        stressHeader.text = "Stress"
        timeHeader.setPadding(16, 8, 16, 8)
        stressHeader.setPadding(16, 8, 16, 8)
        timeHeader.setBackgroundColor(Color.LTGRAY)
        stressHeader.setBackgroundColor(Color.LTGRAY)
        header.addView(timeHeader)
        header.addView(stressHeader)
        table.addView(header)

        // --- Data rows ---
        for (i in stressValues.indices) {
            val row = TableRow(requireContext())
            val timeCell = TextView(requireContext())
            val stressCell = TextView(requireContext())
            timeCell.text = timestamps[i]
            stressCell.text = stressValues[i].toString()
            timeCell.setPadding(16, 8, 16, 8)
            stressCell.setPadding(16, 8, 16, 8)
            row.addView(timeCell)
            row.addView(stressCell)
            table.addView(row)
        }

        return view
    }
}
