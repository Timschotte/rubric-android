package be.hogent.tile3.rubricapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.base.BaseViewModel
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import javax.inject.Inject

class RubricViewModel: ViewModel(){
    @Inject
    lateinit var rubricRepository: RubricRepository

    @Inject
    lateinit var criteriumRepository: CriteriumRepository

    @Inject
    lateinit var niveauRepository: NiveauRepository

    init{
        App.component.inject(this)
    }

    fun getAllRubrics(): LiveData<List<Rubric>>{
        return rubricRepository.getAllRubrics()
    }

    fun getRubric(rubricId: String): Rubric?{
        return rubricRepository.get(rubricId)
    }

    fun insertRubric(rubric: Rubric){
        rubricRepository.insert(rubric)
    }

    fun insertCriterium(criterium: Criterium){
        criteriumRepository.insert(criterium)
    }

    fun insertNiveau(niveau: Niveau){
        niveauRepository.insert(niveau)
    }
}