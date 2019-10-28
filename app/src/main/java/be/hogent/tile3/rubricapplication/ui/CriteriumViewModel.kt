package be.hogent.tile3.rubricapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import javax.inject.Inject

class CriteriumViewModel: ViewModel(){

    @Inject
    lateinit var criteriumRepository: CriteriumRepository


    init{
        App.component.inject(this)
    }


    fun getCriteriaForRubric(rubricId: String): LiveData<List<Criterium>>{
        return criteriumRepository.getCriteriaForRubric(rubricId)
    }

    fun removeAllCriteria(){
        return criteriumRepository.deleteAllCriteria()
    }

    fun insertCriterium(criterium: Criterium){
        return criteriumRepository.insert(criterium)
    }
}