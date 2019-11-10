package be.hogent.tile3.rubricapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.CriteriumOverzichtFragment

class MainActivity : AppCompatActivity(){
    /**
     * Creates the Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // checken op savedInstanceState; anders maak je een nieuw fragment bij het roteren van het
        // scherm waardoor ook de viewmodels opnieuw gemaakt worden en de fragmenten dus geen
        // geen data bijhouden
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, CriteriumOverzichtFragment())
                .commitNow()
        }


    }

    //fun launchRubricsActivity(view: View) {
        //val intent = Intent(this, RubricsActivity::class.java)
        //startActivity(intent)
    //}
}