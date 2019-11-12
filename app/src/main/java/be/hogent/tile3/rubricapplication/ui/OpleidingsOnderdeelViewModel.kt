package be.hogent.tile3.rubricapplication.ui

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


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _opleidingen = MutableLiveData<List<OpleidingsOnderdeel>>()
    val opleidingen: LiveData<List<OpleidingsOnderdeel>>
        get() = _opleidingen

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        App.component.inject(this)
        //getOpleidingen()
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