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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)
        val tableLayout = view.findViewById<TableLayout>(R.id.tableLayout)

        val dummyData = listOf(
            Pair("10:00 AM", "7"),
            Pair("2:00 PM", "4"),
            Pair("7:00 PM", "9")
        )

        for ((time, stress) in dummyData) {
            val row = TableRow(context)
            val t1 = TextView(context)
            val t2 = TextView(context)

            t1.text = time
            t2.text = stress
            t1.setTextColor(Color.BLACK)
            t2.setTextColor(Color.BLACK)
            t1.setPadding(16, 8, 16, 8)
            t2.setPadding(16, 8, 16, 8)

            row.addView(t1)
            row.addView(t2)
            tableLayout.addView(row)
        }

        return view
    }
}
