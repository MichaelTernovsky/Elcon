package com.example.elconproject

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_awg_calculator.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

class AwgCalculator : AppCompatActivity() {
    private var isAwgSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awg_calculator)

        // change background color
        awgCalc.setBackgroundColor(Color.rgb(255, 255, 255))

        // get the action bar element
        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)

        //set title and buttons colors
        titleAwgId.setTextColor(Color.parseColor("#000066"))
        calcAwgId.setBackgroundColor(Color.parseColor("#000066"))
        calcAwgId.setTextColor(Color.parseColor("#FFFFFF"))
        resetAwgId.setBackgroundColor(Color.parseColor("#D5040E"))
        resetAwgId.setTextColor(Color.parseColor("#FFFFFF"))

        // set the spinner values
        val languages = resources.getStringArray(R.array.awp_options)
        if (awgSpinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, languages
            )
            awgSpinner.adapter = adapter
        }

        // set the awg spinner listener
        awgSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // get the selected value
                val selectedItem = awgSpinner.selectedItem.toString()

                // change the txt value according to the selected value
                if (selectedItem == "--Select AWG--") {
                    awg_txt.hint = selectedItem
                    awg_txt.setText("")
                    isAwgSelected = false
                    mm_txt.setText("")
                    mm_2_txt.setText("")
                } else {
                    isAwgSelected = true
                    awg_txt.setText(selectedItem)
                }
            }
        }
    }

    /*
        The back button function
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*
        The function for the reset button.
    */
    fun resetFields(view: View) {
        awgSpinner.setSelection(0)
        isAwgSelected = false
        this.awg_txt.setText("")
        this.mm_txt.setText("")
        this.mm_2_txt.setText("")
    }

    /*
        The function gets a number and round it for 3 digits after the decimal point.
    */
    private fun roundNum(x: Double): Double {
        if (x == -0.0)
            return 0.0

        val df = DecimalFormat("#.###")
        df.roundingMode = RoundingMode.CEILING

        return (df.format(x)).toDouble()
    }

    /*
      The function is printing the correct error message
     */
    private fun printError(message: String) {
        val toast = Toast.makeText(
            applicationContext,
            message, Toast.LENGTH_SHORT
        )
        toast.setGravity(
            Gravity.TOP or Gravity.CENTER_HORIZONTAL,
            0, 200
        )
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 1014)
        toast.show()
    }

    /*
        Check if the input is in the valid range 1-40
    */
    private fun checkRange(str: String): Boolean {
        return (str.toDouble() < 0 || str.toDouble() > 40)
    }

    /*
       The function calculates the values according to the input
    */
    fun calculate(view: View) {
        // get the AWG value
        val n = this.awg_txt.text.toString()

        if (!isAwgSelected && n == "")
            printError("Please select AWG value")
        else if (n != "." && checkRange(n))
            printError("Out of range")
        else {
            when (n) {
                "." -> printError("Invalid value")
                else -> {
                    val mm: Double = when (n) {
                        "00" -> 9.266
                        "000" -> 10.405
                        "0000" -> 11.684
                        else -> 0.127 * 92.0.pow((36.0 - n.toDouble()) / 39.0)
                    }

                    val mm2: Double = when (n) {
                        "00" -> 67.4
                        "000" -> 85.0
                        "0000" -> 107.0
                        else -> 0.012668 * 92.0.pow((36.0 - n.toDouble()) / 19.5)
                    }

                    // update the field
                    this.mm_txt.setText(roundNum(mm).toString())
                    this.mm_2_txt.setText(roundNum(mm2).toString())
                }
            }
        }
    }
}

