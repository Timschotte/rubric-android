package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class LeerlingSelectViewModel(private val rubricId: String, private val opleidingsOnderdeelId: Long) : ViewModel() {

    @Inject
    lateinit var studentRepository: StudentRepository

    @Inject
    lateinit var context: Context

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var studenten: LiveData<List<Student>>

    init{
        App.component.inject(this)
        refreshRubricDatabase()
        studenten = studentRepository.getAllStudentsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        Log.i("test", studenten.toString())
    }

    private fun refreshRubricDatabase() {
        if (isNetworkAvailable()){
            uiScope.launch {
                studentRepository.refreshStudenten()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private val _navigateToRubricView = MutableLiveData<Long>()
    val navigateToRubricView
        get() = _navigateToRubricView


    fun onStudentClicked(id: Long) {
        _navigateToRubricView.value = id
    }

    fun onStudentNavigated(){
        _navigateToRubricView. value = null
    }

}