package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.persistence.EvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.StudentRepository
import be.hogent.tile3.rubricapplication.utils.isNetworkAvailable
import kotlinx.coroutines.*
import java.util.stream.Collectors.toList
import javax.inject.Inject

/**
 * ViewModel for LeerlingSelect
 * @constructor Creates a [LeerlingSelectViewModel]
 * @property rubricId ID for [Rubric]
 * @property opleidingsOnderdeelId OpleidingsOnderdeel
 * @property context [Context]
 * @property studentRepository [StudentRepository]
 * @property evaluatieRepository [EvaluatieRepository]
 * @property viewModelJob [Job] for co-routine
 * @property coroutineScope Scope for co-routine
 * @property studenten [LiveData] [List] of [Student]
 * @property gefilterdeStudenten Filtered [List] of [Student]
 * @property _navigateToRubricView Private indicator for navigation in Fragment
 * @property navigateToRubricView Public getter for [_navigateToRubricView]
 */

class LeerlingSelectViewModel(
    private val rubricId: Long,
    private val opleidingsOnderdeelId: Long
) : ViewModel() {
    /**
     * Properties
     */
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

    private val _evaluaties = MutableLiveData<List<Evaluatie>>()
    val evaluaties: LiveData<List<Evaluatie>>
        get() = _evaluaties

    private val _navigateToRubricView = MutableLiveData<Student>()
    val navigateToRubricView
        get() = _navigateToRubricView

    private val _refreshIsComplete = MutableLiveData<Boolean>(false)
    val refreshIsComplete: LiveData<Boolean>
        get() = _refreshIsComplete
    /**
     * Constructor. Dependency injection. Refreshes rubrics in database, loads the students for current OpleidingsOnderdeel,
     *  refreshes evaluations in database.
     */
    init {
        App.component.inject(this)
        refreshStudentDatabase()
        loadStudentData()
        refreshEvaluationDb()
        gefilterdeStudenten.addSource(studenten){
            gefilterdeStudenten.value = it
        }
    }
    /**
     * Function for retrieving all [Student] for the current OpleidingsOnderdeel as well as all [Evaluatie] for the current Rubric from Room database
     * @see StudentRepository
     * @see EvaluatieRepository
     */
    private fun loadStudentData(){
        studenten = studentRepository.getAllStudentsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        coroutineScope.launch{
            _evaluaties.value = loadEvaluaties()
        }
    }

    private suspend fun loadEvaluaties(): List<Evaluatie> {
        return withContext(Dispatchers.IO){
            evaluatieRepository.getByRubric(rubricId)
        }
    }
    /**
     * Function that filters studenten from SearchBar input on Fragment
     * @param filterText Input to filter the students
     */
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
    /**
     * Co-Routine launcher for synchronizing all [Student] for the current OpleidingsOnderdeel from backend API with Room database
     *  when Network is available
     * @see StudentRepository
     */
    private fun refreshStudentDatabase() {
        if (isNetworkAvailable(context)) {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    studentRepository.refreshStudenten(opleidingsOnderdeelId)
                }.apply {
                    _refreshIsComplete.value = true
                }
            }
        }
    }
    /**
     * Co-Routine for synchronizing all [Evaluatie] for the current [Rubric] and Docent on backend API to Room database.
     *  when Network is available
     * @see EvaluatieRepository
     */
    private fun refreshEvaluationDb(){
        if (isNetworkAvailable(context)) {
            coroutineScope.launch {
                evaluatieRepository.refreshEvaluations(rubricId, 1)
            }
        }
    }
    /**
     * onClickListener handling when a [Student] is clicked in Fragment.
     * @param student [Student]
     */
    fun onStudentClicked(student: Student) {
        _navigateToRubricView.value = student
    }
    /**
     * Resetting the navigation control when navigated in the Fragment
     */
    fun onStudentNavigated() {
        _navigateToRubricView.value = null
    }
    /**
     * Function that is called when the [LeerlingSelectViewModel] is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

/**
 * Data class used to hold a [Student] and its associated [Evaluatie] for the [Rubric] to be passed to LeerlingListAdapter
 * @property student [Student] being evaluated for [Rubric]
 * @property evaluatie [Evaluatie] for the given [Student] and the [Rubric]
 */

data class StudentMetEvaluatie(
    val student: Student,
    val evaluatie: Evaluatie?
)