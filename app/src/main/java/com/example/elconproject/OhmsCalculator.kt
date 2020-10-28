package com.example.elconproject

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ohms_calculator.*
import java.math.RoundingMode
import java.text.DecimalFormat

class OhmsCalculator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ohms_calculator)

        // change background color
        ohmsCalc.setBackgroundColor(Color.rgb(255, 255, 255))

        // get the action bar element
        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)

        //set title and buttons colors
        titleId.setTextColor(Color.parseColor("#000066"))
        calcId.setBackgroundColor(Color.parseColor("#000066"))
        calcId.setTextColor(Color.parseColor("#FFFFFF"))
        resetId.setBackgroundColor(Color.parseColor("#D5040E"))
        resetId.setTextColor(Color.parseColor("#FFFFFF"))
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
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 1163)
        toast.show()
    }

    /*
        The function gets 3 variables and return the one that its value is not 0
     */
    private fun returnNotEmpty(x: String, y: String, z: String): String {
        return when {
            x != "" -> x
            y != "" -> y
            else -> z
        }
    }

    /*
     The function for the reset button.
    */
    fun resetFields(view: View) {
        this.voltTxt.setText("")
        this.currentTxt.setText("")
        this.resistanceTxt.setText("")
        this.powerTxt.setText("")
    }

    /*
        The function gets a number and round it for 3 digits after the decimal point.
     */
    private fun roundNum(x: String): Double {
        val newX = x.toDouble()

        if (newX == -0.0)
            return 0.0

        val df = DecimalFormat("#.#")
        df.roundingMode = RoundingMode.HALF_DOWN

        return (df.format(newX)).toDouble()
    }

    /*
        If x is 6 ---> return 6.0
     */
    private fun addZero(x: String): String {
        return if (x == "" || x == "-" || x == "." || x.contains("."))
            x
        else
            "$x.0"
    }

    /*
        Check the input validity
     */
    private fun checkValidity(str: String): Boolean {
        return (str == "." || str == "-" || str == ".-" || str == "-.")
    }

    /*
        The function checks that there are only 2 values the user insert
    */
    private fun checkTwoValues(a: String, b: String, c: String, d: String): Boolean {
        if (checkValidity(a) || checkValidity(b) || checkValidity(c) || checkValidity(d)) {
            // error - invalid field/s
            printError("Invalid value")
            return false
        } else if (a == "" && b == "" && c == "" && d == "") {
            // error - all the fields are empty
            printError("All fields are empty")
            return false
        } else if (a != "" && b == "" && c == "" && d == "") {
            printError("There are not enough values")
            return false
        } else if (a == "" && b != "" && c == "" && d == "") {
            printError("There are not enough values")
            return false
        } else if (a == "" && b == "" && c != "" && d == "") {
            printError("There are not enough values")
            return false
        } else if (a == "" && b == "" && c == "" && d != "") {
            printError("There are not enough values")
            return false
        } else {
            return if (a != "" && b != "" && c == "" && d == "") // a,b
                true
            else if (a != "" && b == "" && c != "" && d == "") // a,c
                true
            else if (a != "" && b == "" && c == "" && d != "") // a,d
                true
            else if (a == "" && b != "" && c != "" && d == "") // b,c
                true
            else if (a == "" && b != "" && c == "" && d != "") // b,d
                true
            else if (a == "" && b == "" && c != "" && d != "") // c,d
                true
            else {
                printError("There are too many values")
                false
            }
        }
    }

    /*
       The function calculates the values according to the input
     */
    fun calculate(view: View) {
        var v = this.voltTxt.text.toString()
        var i = this.currentTxt.text.toString()
        var r = this.resistanceTxt.text.toString()
        var p = this.powerTxt.text.toString()

        this.voltTxt.setText(addZero(v))
        this.currentTxt.setText(addZero(i))
        this.resistanceTxt.setText(addZero(r))
        this.powerTxt.setText(addZero(p))

        var v1 = ""
        var v2 = ""
        var v3 = ""
        var r1 = ""
        var r2 = ""
        var r3 = ""

        var i1 = ""
        var i2 = ""
        var i3 = ""
        var p1 = ""
        var p2 = ""
        var p3 = ""

        if (checkTwoValues(v, i, r, p)) {
            if (v == "") {
                if (i != "" && r != "")
                    v1 = (i.toDouble() * r.toDouble()).toString()
                if (i != "" && p != "")
                    v2 = (p.toDouble() / i.toDouble()).toString()
                if (r != "" && p != "")
                    v3 = kotlin.math.sqrt((p.toDouble() * r.toDouble())).toString()

                // update the value
                v = returnNotEmpty(v1, v2, v3)
                this.voltTxt.setText(roundNum(v).toString())
            }
            if (i == "") {
                if (v != "" && r != "")
                    i1 = (v.toDouble() / r.toDouble()).toString()
                if (v != "" && p != "")
                    i2 = (p.toDouble() / v.toDouble()).toString()
                if (p != "" && r != "")
                    i3 = kotlin.math.sqrt((p.toDouble() / r.toDouble())).toString()

                // update the value
                i = returnNotEmpty(i1, i2, i3)
                this.currentTxt.setText(roundNum(i).toString())
            }
            if (r == "") {
                if (v != "" && i != "")
                    r1 = (v.toDouble() / i.toDouble()).toString()
                if (i != "" && p != "")
                    r2 = (p.toDouble() / (i.toDouble() * i.toDouble())).toString()
                if (p != "" && v != "")
                    r3 = ((v.toDouble() * v.toDouble()) / p.toDouble()).toString()

                // update the value
                r = returnNotEmpty(r1, r2, r3)
                this.resistanceTxt.setText(roundNum(r).toString())
            }
            if (p == "") {
                if (v != "" && i != "")
                    p1 = (v.toDouble() * i.toDouble()).toString()
                if (i != "" && r != "")
                    p2 = (r.toDouble() * i.toDouble() * i.toDouble()).toString()
                if (r != "" && v != "")
                    p3 = ((v.toDouble() * v.toDouble()) / r.toDouble()).toString()

                // update the value
                p = returnNotEmpty(p1, p2, p3)
                this.powerTxt.setText(roundNum(p).toString())
            }
        }
    }
}