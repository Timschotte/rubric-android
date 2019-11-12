package be.hogent.tile3.rubricapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.CriteriumOverzichtFragment
import be.hogent.tile3.rubricapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){
    /**
     * Creates the Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        // checken op savedInstanceState; anders maak je een nieuw fragment bij het roteren van het
        // scherm waardoor ook de viewmodels opnieuw gemaakt worden en de fragmenten dus geen
        // geen data bijhouden

        // TODO: Kijken wat hier mee moet gebeuren
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main_container, CriteriumOverzichtFragment())
//                .commitNow()
//        }


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

}