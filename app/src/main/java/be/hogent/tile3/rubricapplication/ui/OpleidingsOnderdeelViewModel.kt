package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import kotlinx.coroutines.*
import javax.inject.Inject


class OpleidingsOnderdeelViewModel : ViewModel() {

    @Inject
    lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository
    @Inject
    lateinit var rubricRepository: RubricRepository
    @Inject
    lateinit var context: Context

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _opleidingsOnderdelen: LiveData<List<OpleidingsOnderdeel>>

    val gefilterdeOpleidingsOnderdelen = MediatorLiveData<List<OpleidingsOnderdeel>>()

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        App.component.inject(this)
        refreshRubricDatabase()
        _opleidingsOnderdelen = opleidingsOnderdeelRepository.getAllOpleidingsOnderdelenWithRubric()
        gefilterdeOpleidingsOnderdelen.addSource(_opleidingsOnderdelen){
            gefilterdeOpleidingsOnderdelen.value = it

        }
        Log.i(
            "test",
            opleidingsOnderdeelRepository.getAllOpleidingsOnderdelenWithRubric().toString()
        )
    }

    fun filterChanged(filterText: String?){
        if (filterText != null) {
            _opleidingsOnderdelen.value?.let {
                gefilterdeOpleidingsOnderdelen.value = it.filter { opleidingsOnderdeel ->
                    opleidingsOnderdeel.naam.toLowerCase().contains(filterText.toLowerCase())
                }
            }
            Log.i("test", filterText)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun refreshRubricDatabase() {
        if (isNetworkAvailable()) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    opleidingsOnderdeelRepository.refreshOpleidingsOnderdelen()
                    rubricRepository.refreshRubrics()

                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private val _navigateToRubricSelect = MutableLiveData<Long>()
    val navigateToRubricSelect
        get() = _navigateToRubricSelect

    fun onOpleidingsOnderdeelClicked(id: Long) {
        _navigateToRubricSelect.value = id
    }

    fun onOpleidingsOnderdeelNavigated() {
        _navigateToRubricSelect.value = null
    }

//    private fun getOpleidingen(){
//        uiScope.launch {
//            var getOpleidingenDeferred = OpleidingApi.retrofitService.getProperties()
//            try {
//                var listResult = getOpleidingenDeferred.await()
//                _status.value = "Success: ${listResult.size} opleidingen opgehaald"
//                if (listResult.size > 0){
//                    _opleidingen.value = listResult
//                }
//            } catch (e: Exception){
//                _status.value = "Failure: ${e.message}"
//            }
//        }
//    }


//    private suspend fun insert(opleiding: OpleidingsOnderdeel) {
//        withContext(Dispatchers.IO){
//            database.insert(opleiding)
//        }
//    }

}