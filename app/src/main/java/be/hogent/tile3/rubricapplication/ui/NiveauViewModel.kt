package be.hogent.tile3.rubricapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import javax.inject.Inject

class NiveauViewModel: ViewModel(){

    @Inject
    lateinit var niveauRepository: NiveauRepository


    init{
        App.component.inject(this)
    }

    fun getNiveausForCriterium(criteriumId: String): List<Niveau> {
        return niveauRepository.getNiveausForCriterium(criteriumId)
    }

    fun getNiveausForRubric(rubricId: String): List<Niveau> {
        return niveauRepository.getNiveausForRubric(rubricId)
    }


}