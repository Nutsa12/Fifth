package com.example.task5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.task5.helper.StatisticDb
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var statisticHelper: StatisticDb
    private lateinit var runView: TextView
    private lateinit var swimView: TextView
    private lateinit var calView: TextView
    private lateinit var totalRunView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runView = findViewById<TextView>(R.id.avgRun)
        swimView = findViewById<TextView>(R.id.avgSwim)
        calView = findViewById<TextView>(R.id.avgCal)
        totalRunView = findViewById<TextView>(R.id.totalRun)

        statisticHelper = StatisticDb(this)

        statisticHelper.selectAll()

        loadStatistics()

        findViewById<Button>(R.id.save_btn).setOnClickListener {
            insertStatistic(
                findViewById<TextInputEditText>(R.id.runIpt).text.toString().toDouble(),
                findViewById<TextInputEditText>(R.id.swimInp).text.toString().toDouble(),
                findViewById<TextInputEditText>(R.id.calInp).text.toString().toDouble()
            )
        }
    }

    fun insertStatistic(runStat: Double, swimStat: Double, calStat: Double) {
        if (runStat < 0 || swimStat < 0 || calStat < 0)
            return

        statisticHelper.insertStatistic(
            statisticHelper.getLatestId() + 1,
            runStat,
            swimStat,
            calStat
        )
        clear()
        loadStatistics()
    }

    fun loadStatistics() {
        runView.setText(statisticHelper.getAvgRun().toString())
        swimView.setText(statisticHelper.getAvgSwim().toString())
        calView.setText(statisticHelper.getAvgCal().toString())
        totalRunView.setText(statisticHelper.getTotalRun().toString())
    }

    fun clear() {
        findViewById<TextInputEditText>(R.id.runIpt).setText("")
        findViewById<TextInputEditText>(R.id.swimInp).setText("")
        findViewById<TextInputEditText>(R.id.calInp).setText("")
    }
}