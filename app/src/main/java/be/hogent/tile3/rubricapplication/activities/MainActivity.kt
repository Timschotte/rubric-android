package be.hogent.tile3.rubricapplication.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.ActivityMainBinding
import be.hogent.tile3.rubricapplication.fragments.CriteriumEvaluatieFragment
import be.hogent.tile3.rubricapplication.fragments.MainFragment

class MainActivity : AppCompatActivity(){
    /**
     * Creates the Activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

//        //val mainFragment: MainFragment = MainFragment()
//        val mainFragment: CriteriumEvaluatieFragment = CriteriumEvaluatieFragment()
//
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.main_container, mainFragment)
//            .commit()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    //fun launchRubricsActivity(view: View) {
        //val intent = Intent(this, RubricsActivity::class.java)
        //startActivity(intent)
    //}
}