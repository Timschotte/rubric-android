package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.model.Docent
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel

class CriteriumOverzichtViewModelFactory (private val rubricId: Long, private val student: Student) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CriteriumOverzichtViewModel::class.java)) {
            return CriteriumOverzichtViewModel(rubricId, student) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}