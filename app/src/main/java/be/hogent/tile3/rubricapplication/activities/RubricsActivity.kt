package be.hogent.tile3.rubricapplication.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.fragments.RubricFragment
import be.hogent.tile3.rubricapplication.model.Rubric

class RubricsActivity : AppCompatActivity() {
    private lateinit var rubric: Rubric

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rubrics)
        rubric = intent.getParcelableExtra<Rubric>("rubric")

        val rubricFragment: RubricFragment = RubricFragment.newInstance(rubric)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, rubricFragment)
            .commit()
    }
}