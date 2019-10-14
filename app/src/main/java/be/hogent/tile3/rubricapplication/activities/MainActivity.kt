package be.hogent.tile3.rubricapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import be.hogent.tile3.rubricapplication.R

class MainActivity : AppCompatActivity(){
    /**
     * Creates the Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun launchRubricsActivity(view: View) {
        val intent = Intent(this, RubricsActivity::class.java)
        startActivity(intent)
    }
}