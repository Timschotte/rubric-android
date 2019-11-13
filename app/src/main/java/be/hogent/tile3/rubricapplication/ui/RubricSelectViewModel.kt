package be.hogent.tile3.rubricapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import java.util.*
import javax.inject.Inject

class RubricSelectViewModel(private val opleidingsOnderdeelId: Long) : ViewModel() {

    @Inject
    lateinit var rubricRepository: RubricRepository

    @Inject
    lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository


    var rubrics: LiveData<List<Rubric>>

    val opleidingsOnderdeel = MediatorLiveData<OpleidingsOnderdeel>()

    init{
        App.component.inject(this)
        opleidingsOnderdeel.addSource(opleidingsOnderdeelRepository.get(opleidingsOnderdeelId), opleidingsOnderdeel::setValue)
        rubrics = rubricRepository.getAllRubricsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
    }

    private fun getDummyRubrics(): MutableLiveData<List<Rubric>>{
        var result = MutableLiveData<List<Rubric>>()
        result.value = listOf(
            Rubric(1.toString(),"Analyse 1", "", Date().toString(), Date().toString(), 1),
            Rubric(1.toString(),"Analyse 2", "", Date().toString(), Date().toString(), 1),
            Rubric(1.toString(),"Analyse 3", "", Date().toString(), Date().toString(), 1),
            Rubric(2.toString(),"WebApps 1", "", Date().toString(), Date().toString(), 2),
            Rubric(2.toString(),"WebApps 2", "", Date().toString(), Date().toString(), 2),
            Rubric(2.toString(),"WebApps 3", "", Date().toString(), Date().toString(), 2),
            Rubric(2.toString(),"WebApps 4", "", Date().toString(), Date().toString(), 2),
            Rubric(3.toString(),"Bachelorproef", "", Date().toString(), Date().toString(), 3),
            Rubric(4.toString(),"Databanken 1", "", Date().toString(), Date().toString(), 4),
            Rubric(4.toString(),"Databanken 2", "", Date().toString(), Date().toString(), 4),
            Rubric(4.toString(),"Databanken 3", "", Date().toString(), Date().toString(), 4),
            Rubric(5.toString(),"Programmeren 1", "", Date().toString(), Date().toString(), 5),
            Rubric(5.toString(),"Programmeren 2", "", Date().toString(), Date().toString(), 5),
            Rubric(5.toString(),"Programmeren 3", "", Date().toString(), Date().toString(), 5)
        )
        return result
    }
}