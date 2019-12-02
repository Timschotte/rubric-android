package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.persistence.StudentRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class LeerlingSelectViewModel(
    private val rubricId: String,
    private val opleidingsOnderdeelId: Long
) : ViewModel() {

    @Inject
    lateinit var studentRepository: StudentRepository

    @Inject
    lateinit var context: Context

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _studenten: LiveData<List<Student>>

    val gefilterdeStudenten = MediatorLiveData<List<Student>>()

    init {
        App.component.inject(this)
        refreshRubricDatabase()
        _studenten = studentRepository.getAllStudentsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        gefilterdeStudenten.addSource(_studenten){
            gefilterdeStudenten.value = it
        }
        Log.i("test2", _studenten.toString())
    }

    fun filterChanged(filterText: String?){
        if (filterText != null) {
            _studenten.value?.let {
                gefilterdeStudenten.removeSource(_studenten)
                gefilterdeStudenten.addSource(_studenten){
                    gefilterdeStudenten.value = it.filter { student ->
                        student.studentNaam.toLowerCase().contains(filterText.toLowerCase())
                    }
                }
            }
            Log.i("test", filterText)
        }
    }

    private fun refreshRubricDatabase() {
        if (isNetworkAvailable()) {
            coroutineScope.launch {
                studentRepository.refreshStudenten()
            }
        }
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private val _navigateToRubricView = MutableLiveData<Student>()
    val navigateToRubricView
        get() = _navigateToRubricView


    fun onStudentClicked(student: Student) {
        _navigateToRubricView.value = student
    }

    fun onStudentNavigated() {
        _navigateToRubricView.value = null
    }

}