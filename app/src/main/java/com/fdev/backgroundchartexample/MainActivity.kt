package com.fdev.backgroundchartexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gradientChart.chartValues = arrayOf(
            40f, 40f, 40f, 32f, 13f, 5f, 18f, 36f, 20f, 30f, 28f, 27f, 29f
        )
    }


}
