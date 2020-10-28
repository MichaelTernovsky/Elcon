package com.example.elconproject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_pt100_calculator.*
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import java.math.RoundingMode
import java.text.DecimalFormat

class Pt100Calculator : AppCompatActivity() {
    private var ptKind: String = ""
    private var arrowSide: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt100_calculator)

        // change background color
        pt100Calc.setBackgroundColor(Color.rgb(255, 255, 255))

        // get the action bar element
        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)

        //set title and buttons colors
        titlePTId.setTextColor(Color.parseColor("#000066"))
        calcPTId.setBackgroundColor(Color.parseColor("#000066"))
        calcPTId.setTextColor(Color.parseColor("#FFFFFF"))
        resetPTId.setBackgroundColor(Color.parseColor("#D5040E"))
        resetPTId.setTextColor(Color.parseColor("#FFFFFF"))

        // check which radio was chosen
        checkPT.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                ptKind = "${radio.text}"
            })


        // set up image visibility to false
        up_arrowId.visibility = View.GONE
        arrowSide = "down"
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
        this.tempPT_txt.setText("")
        this.resPT_txt.setText("")
    }

    /*
        The function gets a number and round it for 3 digits after the decimal point.
    */
    private fun roundNum(x: Double): Double {
        if (x == -0.0)
            return 0.0

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING

        return (df.format(x)).toDouble()
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
        toast.setGravity(Gravity.TOP or Gravity.CENTER, 0, 1002)
        toast.show()

        //this.resPT_txt.setText("")
    }

    /*
        The function checks the range of the resistance
     */
    private fun checkRange(res: Double): Boolean {
        return when (ptKind) {
            "PT100" -> (res in 18.547..390.477)
            "PT500" -> (res in 92.735..1952.385)
            "PT1000" -> (res in 185.47..3904.77)
            else -> {
                false
            }
        }
    }

    /*
       The function calculates the values according to the input
     */
    fun calculate(view: View) {
        var radioFlag = false
        val res = this.resPT_txt.text.toString()
        val temp = this.tempPT_txt.text.toString()

        this.resPT_txt.setText(addZero(res))
        this.tempPT_txt.setText(addZero(temp))

        if (checkValidity(res) || checkValidity(temp)) {
            // error - invalid field/s
            printError("Invalid values")
        } else {
            if (ptKind != "") {
                // resistance at temperature 0
                var r0 = 100.0

                // constants
                val a = 3.9083e-3
                val b = -5.7750e-7
                var c = -4.1830e-12

                // set the resistance by the user choice
                when (ptKind) {
                    "PT100" -> r0 = 100.0
                    "PT500" -> r0 = 500.0
                    "PT1000" -> r0 = 1000.0
                    else -> {
                        radioFlag = true
                        printError("Select PT kind")
                    }
                }

                if (arrowSide == "down") {
                    if (temp == "")
                        printError("Insert temperature")
                    else {
                        // resistance at temperature t - want to calculate
                        val rt: Double

                        if (radioFlag)
                            return

                        // the temperature
                        val t = temp.toDouble()

                        if (temp.toDouble() >= 0.0)
                            c = 0.0

                        if (temp.toDouble() <= 850 && temp.toDouble() >= -200) {
                            // the calculations
                            rt = r0 *
                                    (1 + (a * t) +
                                            (b * t * t) +
                                            (c * (t - 100.0) * (t * t * t)))

                            // set the new value
                            resPT_txt.setText(roundNum(rt).toString())
                        } else printError("The temperature is out of range")
                    }
                } else if (arrowSide == "up") {
                    if (res == "")
                        printError("Insert resistance")
                    else {
                        if (radioFlag)
                            return

                        if (checkRange(res.toDouble())) {
                            val temperature =
                                (-r0 * a + kotlin.math.sqrt(
                                    (r0 * r0 * a * a) -
                                            (4 * r0 * b * (r0 - res.toDouble()))
                                )) / (2 * r0 * b)

                            // set the new value
                            tempPT_txt.setText(roundNum(temperature).toString())
                        } else
                            printError("The resistance is out of range")
                    }
                }
            } else
                printError("Select PT kind")
        }
    }

    /*
        The function changes the image
    */
    fun changeImage(view: View) {
        // reset fields
        resetFields(view)

        // change the image
        if (arrowSide == "down") {
            up_arrowId.visibility = View.VISIBLE
            down_arrowId.visibility = View.GONE

            arrowSide = "up"
        } else {
            down_arrowId.visibility = View.VISIBLE
            up_arrowId.visibility = View.GONE

            arrowSide = "down"
        }
    }
}