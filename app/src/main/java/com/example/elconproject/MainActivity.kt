package com.example.elconproject

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // change background color
        openingScreen.setBackgroundColor(Color.rgb(255, 255, 255))

        // get the action bar element
        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)

        // ohmsCalcTitle case sensitive
        ohmsCalcTitle.transformationMethod = null
    }

    /*
    Open phone keyboard when pressing
     */
    fun openPhone(view: View) {
        val phone = "08-9433630"
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        startActivity(intent)
    }

    /*
        Open the website url pressing
     */
    fun openWebsite(view: View) {
        val url = "http://www.elcon.co.il"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    /*
        The function redirects to the ohms calc page
    */
    fun ohmsCalcLink(view: View) {
        val intent = Intent(this, OhmsCalculator::class.java).apply { }
        startActivity(intent)
    }

    /*
        The function redirects to the pt100 calc page
    */
    fun pt100CalcLink(view: View) {
        val intent = Intent(this, Pt100Calculator::class.java).apply { }
        startActivity(intent)
    }

    /*
        The function redirects to the AWG calc page
    */
    fun awgCalcLink(view: View) {
        val intent = Intent(this, AwgCalculator::class.java).apply { }
        startActivity(intent)
    }

    /*
        The function redirects to the Thermocouple Codes Table page
    */
    fun thermocoupleCodesTableLink(view: View) {
        val intent = Intent(this, ThermocoupleCodesTable::class.java).apply { }
        startActivity(intent)
    }
}