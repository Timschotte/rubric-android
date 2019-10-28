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
import be.hogent.tile3.rubricapplication.data.RubricData
import be.hogent.tile3.rubricapplication.data.RubricsResource
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.ui.CriteriumViewModel
import be.hogent.tile3.rubricapplication.ui.MainViewModel
import be.hogent.tile3.rubricapplication.ui.NiveauViewModel
import be.hogent.tile3.rubricapplication.ui.RubricViewModel
import be.hogent.tile3.rubricapplication.utils.RubricDataToRubricMapper
import kotlinx.android.synthetic.main.rubric_list.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onComplete

class MainFragment: Fragment() {

    private lateinit var  mainViewModel: MainViewModel
    private lateinit var  rubricViewModel: RubricViewModel
    private lateinit var  criteriumViewModel: CriteriumViewModel
    private lateinit var  niveauViewModel: NiveauViewModel
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
        criteriumViewModel = ViewModelProviders.of(activity!!).get(CriteriumViewModel::class.java)
        niveauViewModel = ViewModelProviders.of(activity!!).get(NiveauViewModel::class.java)

        //comment for demo
        deleteAllData()

        adapter = RubricRecyclerViewAdapter(MainRubricListener { rubric ->
            val intent = Intent(activity,RubricsActivity::class.java)
            intent.putExtra("rubric", rubric)
            startActivity(intent)
        })

        rubricViewModel.getAllRubrics().observe(this, Observer<List<Rubric>> {
            adapter.data = it
        })

        //Observes the livedate from the viewmodel and displays it when it changes
        val rubricObjectObserver = Observer<List<RubricData>> { rubricData ->
            processNewRubrics(rubricData)

        }

        //attach the observer to the livedata
        mainViewModel.getRubricDataObject().observe(this, rubricObjectObserver)
        rubric_recyclerView.adapter = adapter
        rubric_recyclerView.layoutManager = LinearLayoutManager(activity)
    }

    private fun processNewRubrics(rubricData: List<RubricData>) {
        val rubrics = RubricDataToRubricMapper.getRubricModels(rubricData)
        val criteria = RubricDataToRubricMapper.getCriteriumModels(rubricData)
        val niveaus = RubricDataToRubricMapper.getNiveauModels(rubricData)
        insertModelsInDatabank(rubrics, criteria, niveaus)
    }

    private fun insertModelsInDatabank(
        rubrics: List<Rubric>,
        criteria: List<Criterium>,
        niveaus: List<Niveau>
    ) {
        doAsync {
            for (rubric in rubrics) {
                rubricViewModel.insertRubric(rubric)
            }
            onComplete {
                doAsync {
                    for (criterium in criteria) {
                        criteriumViewModel.insertCriterium(criterium)
                    }
                    onComplete {
                        doAsync {
                            for (niveau in niveaus) {
                                niveauViewModel.insertNiveau(niveau)
                            }
                        }
                    }
                }

            }
        }
    }

    private fun deleteAllData() {
        doAsync {
            niveauViewModel.removeAllNiveaus()
            onComplete {
                doAsync {
                    criteriumViewModel.removeAllCriteria()
                    onComplete {
                        doAsync {
                            rubricViewModel.removeAllRubrics()
                        }
                    }
                }

            }
        }
    }
}