package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import kotlinx.coroutines.*
import javax.inject.Inject


class OpleidingsOnderdeelViewModel: ViewModel() {

    @Inject lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository
    @Inject lateinit var context: Context

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var opleidingsOnderdelen: LiveData<List<OpleidingsOnderdeel>>

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        App.component.inject(this)
        refreshRubricDatabase()
        opleidingsOnderdelen = opleidingsOnderdeelRepository.getAllOpleidingsOnderdelen()
        Log.i("test", opleidingsOnderdeelRepository.getAllOpleidingsOnderdelen().toString())
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun refreshRubricDatabase() {
        if (isNetworkAvailable()){
            uiScope.launch {
                opleidingsOnderdeelRepository.refreshOpleidingsOnderdelen()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private val _navigateToRubricSelect = MutableLiveData<Long>()
    val navigateToRubricSelect
        get() = _navigateToRubricSelect

    fun onOpleidingsOnderdeelClicked(id: Long) {
        _navigateToRubricSelect.value = id
    }

    fun onOpleidingsOnderdeelNavigated(){
        _navigateToRubricSelect. value = null
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