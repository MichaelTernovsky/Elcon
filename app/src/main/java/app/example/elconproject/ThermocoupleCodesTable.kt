package app.example.elconproject

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.elconproject.R
import kotlinx.android.synthetic.main.activity_awg_calculator.*
import kotlinx.android.synthetic.main.activity_thermocouple_codes_table.*

class ThermocoupleCodesTable : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thermocouple_codes_table)

        // change background color
        thermocoupleCodesTbl.setBackgroundColor(Color.rgb(255, 255, 255))

        // change title color
        titleThermoCodesId.setTextColor(Color.parseColor("#000066"))

        // get the action bar element
        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)
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
}