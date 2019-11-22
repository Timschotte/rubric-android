package be.hogent.tile3.rubricapplication.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject

class CriteriumSelectViewModel(
    private val rubricId: String,
    private val studentId: Long
) : ViewModel() {
    @Inject
    lateinit var context: Context

    private var viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init{
        //Create a temporary evaluation and criteriumEvaluations
        //List them below
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}