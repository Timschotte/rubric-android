package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.persistence.EvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.StudentRepository
import be.hogent.tile3.rubricapplication.utils.isNetworkAvailable
import kotlinx.coroutines.*
import javax.inject.Inject

class LeerlingSelectViewModel(
    private val rubricId: Long,
    private val opleidingsOnderdeelId: Long
) : ViewModel() {

    @Inject
    lateinit var studentRepository: StudentRepository

    @Inject
    lateinit var evaluatieRepository: EvaluatieRepository

    @Inject
    lateinit var context: Context

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    lateinit var studenten: LiveData<List<Student>>

    val gefilterdeStudenten = MediatorLiveData<List<Student>>()

    init {
        App.component.inject(this)
        refreshStudentDatabase()
        loadStudents()
        refreshEvaluationDb()
        gefilterdeStudenten.addSource(studenten){
            gefilterdeStudenten.value = it
        }
    }

    private fun loadStudents(){
        studenten = studentRepository.getAllStudentsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
    }

    fun filterChanged(filterText: String?){
        if (filterText != null) {
            studenten.value?.let {
                gefilterdeStudenten.removeSource(studenten)
                gefilterdeStudenten.addSource(studenten){
                    gefilterdeStudenten.value = it.filter { student ->
                        student.studentNaam.toLowerCase().contains(filterText.toLowerCase())
                    }
                }
            }
        }
    }

    private fun refreshStudentDatabase() {
        if (isNetworkAvailable(context)) {
            coroutineScope.launch {
                studentRepository.refreshStudenten(opleidingsOnderdeelId)
            }
        }
    }

    private fun refreshEvaluationDb(){
        if (isNetworkAvailable(context)) {
            coroutineScope.launch {
                evaluatieRepository.refreshEvaluations(rubricId, 1)
            }
        }
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