package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class RubricSelectViewModel(opleidingsOnderdeelId: Long) : ViewModel() {

    @Inject
    lateinit var rubricRepository: RubricRepository

    @Inject
    lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository

    @Inject lateinit var context: Context

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _rubrics: LiveData<List<Rubric>>
    val gefilterdeRubrics = MediatorLiveData<List<Rubric>>()

    val opleidingsOnderdeel = MediatorLiveData<OpleidingsOnderdeel>()

    init{
        App.component.inject(this)
        opleidingsOnderdeel.addSource(opleidingsOnderdeelRepository.get(opleidingsOnderdeelId), opleidingsOnderdeel::setValue)
        //refreshRubricDatabase()
        _rubrics = rubricRepository.getAllRubricsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        gefilterdeRubrics.addSource(_rubrics){
            gefilterdeRubrics.value = it
        }
        Log.i("test2", _rubrics.toString())
    }

    fun filterChanged(filterText: String?){
        if (filterText != null) {
            _rubrics.value?.let {
                gefilterdeRubrics.removeSource(_rubrics)
                gefilterdeRubrics.addSource(_rubrics){
                    gefilterdeRubrics.value = it.filter { rubric ->
                        rubric.onderwerp.toLowerCase().contains(filterText.toLowerCase())
                    }
                }
            }
        }
    }
    private fun refreshRubricDatabase() {
        if (isNetworkAvailable()){
            uiScope.launch {
                deleteRubricsRoutine()
                refreshRubricsRoutine()
        }
    }
    suspend fun deleteRubricsRoutine(){
        withContext(Dispatchers.IO){
            rubricRepository.deleteAllRubrics()
        }
    }
    suspend fun refreshRubricsRoutine(){
        withContext(Dispatchers.IO){
            rubricRepository.refreshRubrics()
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private val _navigateToKlasSelect = MutableLiveData<String>()
    val navigateToKlasSelect
        get() = _navigateToKlasSelect


    fun onRubricClicked(id: String) {
        _navigateToKlasSelect.value = id
    }

    fun onOpleidingsOnderdeelNavigated(){
        _navigateToKlasSelect. value = null
    }

}