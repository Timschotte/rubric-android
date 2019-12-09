package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import be.hogent.tile3.rubricapplication.ui.RubricSelectViewModel
/**
 * ViewModelFactory for creating instance of [RubricSelectViewModel] build around a given OpleidingsOnderdeel
 * @param opleidingsOnderdeelId ID from given OpleidingsOnderdeel
 * @see ViewModelProvider.Factory
 */
class RubricSelectViewModelFactory(private val opleidingsOnderdeelId: Long) : ViewModelProvider.Factory {
    /**
     * Function that creates the instance of [RubricSelectViewModel]
     * @param modelClass ViewModelClass
     * @return [RubricSelectViewModel]
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RubricSelectViewModel::class.java)) {
            return RubricSelectViewModel(opleidingsOnderdeelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}