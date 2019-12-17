package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import be.hogent.tile3.rubricapplication.utils.isNetworkAvailable
import kotlinx.coroutines.*
import javax.inject.Inject

enum class ApiStatus { LOADING, ERROR, DONE }

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

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
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

    /**
     * Removing and re-adding source is to avoid that filtered items reappear when source list is updated
     */
    fun filterChanged(filterText: String?){
        if (filterText != null) {
            _opleidingsOnderdelen.value?.let {
                gefilterdeOpleidingsOnderdelen.removeSource(_opleidingsOnderdelen)
                gefilterdeOpleidingsOnderdelen.addSource(_opleidingsOnderdelen){
                    gefilterdeOpleidingsOnderdelen.value = it.filter { opleidingsOnderdeel ->
                        opleidingsOnderdeel.naam.toLowerCase().contains(filterText.toLowerCase())
                }
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
        if (isNetworkAvailable(context)) {
            uiScope.launch {
                try {
                    _status.value = ApiStatus.LOADING
                    val refresh = async(Dispatchers.IO) {
                        opleidingsOnderdeelRepository.refreshOpleidingsOnderdelen()
                        rubricRepository.refreshRubrics()
                    }
                    refresh.await()
                    println("Done refreshing")
                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    println("Error refreshing")
                }

            }
        }
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

}