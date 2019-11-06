package be.hogent.tile3.rubricapplication.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.hogent.tile3.rubricapplication.dao.OpleidingDao
import be.hogent.tile3.rubricapplication.ui.OpleidingViewModel
import java.lang.IllegalArgumentException

class OpleidingViewModelFactory(
        private val dataSource: OpleidingDao,
        private val application: Application): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(OpleidingViewModel::class.java)) {
            return OpleidingViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
