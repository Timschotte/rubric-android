package be.hogent.tile3.rubricapplication.activities

import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.databinding.ActivityMainBinding

/**
 * Main Activity extends AppCompatActivity
 *
 */
class MainActivity : AppCompatActivity(){

    /**
     * Creates the Main Activity (Created state).
     * Initializes binding, navController and set up actionbar for MainActivity.
     * @params savedInstanceState Bundle for savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    /**
     * This method is called whenever the user chooses to navigate Up within MainActivity's hierarchy from the action bar.
     * @return Boolean
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }
    /**
     * Prepare options menu to be displayed on MainActivity
     * @params menu Menu to be displayed
     * @return Boolean
     */
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val searchBarOpleiding =
            menu?.findItem(R.id.action_search)?.actionView as androidx.appcompat.widget.SearchView?
        val editText = searchBarOpleiding?.findViewById(R.id.search_src_text) as EditText?
        editText?.text?.clear()
        return super.onPrepareOptionsMenu(menu)
    }

}