package be.hogent.tile3.rubricapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.RubricFragment

class RubricsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rubrics)

        val rubricFragment: RubricFragment = RubricFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, rubricFragment)
            .commit()
    }
}