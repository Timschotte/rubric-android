package be.hogent.tile3.rubricapplication.ui

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.activities.RubricsActivity
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import javax.inject.Inject

class MainViewModel: ViewModel(){

    @Inject
    lateinit var rubricRepository: RubricRepository

    init {
        App.component.inject(this)
    }

    fun getRubrics():LiveData<List<Rubric>>{
        return rubricRepository.getAllRubrics()
    }

    fun onGetRubric() {

    }

}