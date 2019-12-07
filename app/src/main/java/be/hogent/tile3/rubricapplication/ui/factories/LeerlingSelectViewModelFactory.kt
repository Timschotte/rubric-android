package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.ui.LeerlingSelectViewModel

class LeerlingSelectViewModelFactory(private val rubricId: Long, private val opleidingsOnderdeelId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeerlingSelectViewModel::class.java)) {
            return LeerlingSelectViewModel(rubricId, opleidingsOnderdeelId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}