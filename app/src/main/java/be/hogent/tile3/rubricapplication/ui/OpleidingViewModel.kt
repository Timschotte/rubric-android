package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel

class OpleidingViewModel: ViewModel() {
    init {
        Log.i("OpleidingViewModel", "OpleidingViewModel created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("OpleidingViewModel", "OpleidingViewModel destroyed")
    }
}