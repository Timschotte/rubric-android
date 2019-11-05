package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class OpleidingViewModel: ViewModel() {

    private var opleiding = ""

    var opleidingList = mutableListOf("Opleiding 1", "Opleiding 2", "Opleiding 3", "Opleiding 4")



    init {
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("OpleidingViewModel", "OpleidingViewModel destroyed")

    }

}