package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import be.hogent.tile3.rubricapplication.ui.LeerlingSelectViewModel
/**
 * ViewModelFactory for creating instance of [LeerlingSelectViewModel] build around a given Rubric and OpleidingsOnderdeel
 * @param rubricId ID from given Rubric
 * @param opleidingsOnderdeelId ID from given OpleidingsOnderdeel
 * @see ViewModelProvider.Factory
 */
class LeerlingSelectViewModelFactory(private val rubricId: Long, private val opleidingsOnderdeelId: Long) : ViewModelProvider.Factory {
    /**
     * Function that creates the instance of [LeerlingSelectViewModel]
     * @param modelClass ViewModelClass
     * @return [LeerlingSelectViewModel]
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeerlingSelectViewModel::class.java)) {
            return LeerlingSelectViewModel(rubricId, opleidingsOnderdeelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}