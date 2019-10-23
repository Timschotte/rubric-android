package be.hogent.tile3.rubricapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.activities.RubricsActivity
import be.hogent.tile3.rubricapplication.adapters.MainRubricListener
import be.hogent.tile3.rubricapplication.adapters.RubricRecyclerViewAdapter
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.ui.MainViewModel
import be.hogent.tile3.rubricapplication.ui.RubricViewModel
import kotlinx.android.synthetic.main.rubric_list.*
import org.jetbrains.anko.doAsync

class MainFragment: Fragment() {

    private lateinit var  mainViewModel: MainViewModel
    private lateinit var  rubricViewModel: RubricViewModel
    private lateinit var adapter: RubricRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        rubricViewModel = ViewModelProviders.of(activity!!).get(RubricViewModel::class.java)

        insertSampleData()

        adapter = RubricRecyclerViewAdapter(MainRubricListener { rubric ->
            val intent = Intent(activity,RubricsActivity::class.java)
            startActivity(intent);
        })
        rubricViewModel.getAllRubrics().observe(this, Observer<List<Rubric>> {
            adapter.data = it
        })

        rubric_recyclerView.adapter = adapter
        rubric_recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun insertSampleData(){
        doAsync {
            rubricViewModel.insertRubric(Rubric("1","Android","Het vak native apps I", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("2","IOS","Het vak native apps II", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("3","Projecten","Het vak native apps III", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("4","Onderzoekstechnieken","Het vak onderzoekstechnieken", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("5","Probleemoplossend denken I","Het vak probleemoplossend denken I", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("6","Probleemoplossend denken II","Het vak probleemoplossend denken II", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("7","Android","Het vak native apps III", "01012019","01012019"))
            rubricViewModel.insertRubric(Rubric("8","Android","Het vak native apps IV", "01012019","01012019"))
        }
    }


}