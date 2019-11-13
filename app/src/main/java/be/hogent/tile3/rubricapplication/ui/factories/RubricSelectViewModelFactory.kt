package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.ui.RubricSelectViewModel

class RubricSelectViewModelFactory(private val opleidingsOnderdeelId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RubricSelectViewModel::class.java)) {
            return RubricSelectViewModel(opleidingsOnderdeelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}