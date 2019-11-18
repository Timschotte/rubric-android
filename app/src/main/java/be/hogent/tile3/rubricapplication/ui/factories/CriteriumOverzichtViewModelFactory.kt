package be.hogent.tile3.rubricapplication.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.ui.CriteriumOverzichtViewModel
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID

class CriteriumOverzichtViewModelFactory(
    private val rubricId: String,
    private val studentId: Long,
    private val evaluatieId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CriteriumOverzichtViewModel::class.java)) {
            return if (evaluatieId == null || evaluatieId.equals(TEMP_EVALUATIE_ID))
                CriteriumOverzichtViewModel(rubricId, studentId) as T
            else
                CriteriumOverzichtViewModel(rubricId, studentId, evaluatieId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}