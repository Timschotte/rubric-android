package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.SyncStatus
import be.hogent.tile3.rubricapplication.persistence.NetworkStateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncStateViewModel : ViewModel() {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var networkStateRepository: NetworkStateRepository

    private var viewModelJob = Job()
    private val ioScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    val _networkState = MutableLiveData<SyncStatus>()
    val networkState: LiveData<SyncStatus>
        get() = _networkState

    init {
        App.component.inject(this)
        getNetworkState()
    }

    fun getNetworkState(){
        ioScope.launch {
            val currentState = networkStateRepository.currentState
            currentState.let{
                _networkState.postValue(it.value)
            }
        }
    }
}