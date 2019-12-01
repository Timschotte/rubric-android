package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class RubricSelectViewModel(opleidingsOnderdeelId: Long) : ViewModel() {

    @Inject
    lateinit var rubricRepository: RubricRepository

    @Inject
    lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository

    @Inject lateinit var context: Context

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var rubrics: LiveData<List<Rubric>>

    val opleidingsOnderdeel = MediatorLiveData<OpleidingsOnderdeel>()

    init{
        App.component.inject(this)
        opleidingsOnderdeel.addSource(opleidingsOnderdeelRepository.get(opleidingsOnderdeelId), opleidingsOnderdeel::setValue)
        //refreshRubricDatabase()
        rubrics = rubricRepository.getAllRubricsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        Log.i("test2", rubrics.toString())
    }

//    private fun refreshRubricDatabase() {
//        if (isNetworkAvailable()){
//            uiScope.launch {
//                //rubricRepository.refreshRubrics()
//            }
//        }
//    }

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