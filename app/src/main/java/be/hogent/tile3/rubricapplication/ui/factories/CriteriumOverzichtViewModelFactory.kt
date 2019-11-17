package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel

class CriteriumOverzichtViewModelFactory (private val rubricId: String, private val studentId: Long, private val evaluatieId: Long?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CriteriumOverzichtViewModel::class.java)) {
            return CriteriumOverzichtViewModel(rubricId, studentId, evaluatieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}