package be.hogent.tile3.rubricapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.hogent.tile3.rubricapplication.dao.OpleidingDao
import be.hogent.tile3.rubricapplication.model.Opleiding
import be.hogent.tile3.rubricapplication.network.OpleidingApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import javax.security.auth.callback.Callback


class OpleidingViewModel(val database: OpleidingDao,
                         application: Application): AndroidViewModel(application) {


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _opleidingen = MutableLiveData<List<Opleiding>>()
    val opleidingen: LiveData<List<Opleiding>>
        get() = _opleidingen

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        getOpleidingen()
    }

    private fun getOpleidingen(){
        uiScope.launch {
            var getOpleidingenDeferred = OpleidingApi.retrofitService.getProperties()
            try {
                var listResult = getOpleidingenDeferred.await()
                _status.value = "Success: ${listResult.size} opleidingen opgehaald"
                if (listResult.size > 0){
                    _opleidingen.value = listResult
                }
            } catch (e: Exception){
                _status.value = "Failure: ${e.message}"
            }
        }
    }


    private suspend fun insert(opleiding: Opleiding) {
        withContext(Dispatchers.IO){
            database.insert(opleiding)
        }
    }

}