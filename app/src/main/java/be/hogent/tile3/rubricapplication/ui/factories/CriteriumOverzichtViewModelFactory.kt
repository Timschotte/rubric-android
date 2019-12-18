package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel

/**
 * ViewModelFactory for creating instance of [CriteriumOverzichtViewModel] build around a given Rubric and [Student]
 * @param rubricId ID from given Rubric
 * @param student [Student]
 * @see ViewModelProvider.Factory
 */
class CriteriumOverzichtViewModelFactory (private val rubricId: Long, private val student: Student) : ViewModelProvider.Factory {
    /**
     * Function that creates the instance of [CriteriumOverzichtViewModel]
     * @param modelClass ViewModelClass
     * @return [CriteriumOverzichtViewModel]
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CriteriumOverzichtViewModel::class.java)) {
            return CriteriumOverzichtViewModel(rubricId, student) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}