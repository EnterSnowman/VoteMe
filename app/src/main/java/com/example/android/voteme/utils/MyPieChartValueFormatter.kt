package com.example.android.voteme.utils

import android.util.Log
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DecimalFormat

/**
 * Created by Valentin on 23.08.2017.
 */
class MyPieChartValueFormatter : IValueFormatter {
    var mDecimalFormat: DecimalFormat
    var mPercentFormat : DecimalFormat

    init {
        mDecimalFormat = DecimalFormat("###,###,###,###")
        mPercentFormat = DecimalFormat("###.#")
    }
    override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
        Log.d("PieChart",entry?.x.toString()+" "+entry?.y.toString())
        return mDecimalFormat.format(entry?.y)+" - "+mPercentFormat.format(value)+"%"
    }
}