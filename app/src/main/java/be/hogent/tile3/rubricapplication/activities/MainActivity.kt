package be.hogent.tile3.rubricapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.MainFragment

class MainActivity : AppCompatActivity(){
    /**
     * Creates the Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment: MainFragment = MainFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, mainFragment)
            .commit()

    }

    //fun launchRubricsActivity(view: View) {
        //val intent = Intent(this, RubricsActivity::class.java)
        //startActivity(intent)
    //}
}