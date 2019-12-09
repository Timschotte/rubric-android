package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import be.hogent.tile3.rubricapplication.utils.isNetworkAvailable
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * ViewModel for OpleidingsOnderdeel
 * @constructor Creates a [OpleidingsOnderdeelViewModel]
 * @property opleidingsOnderdeelRepository [OpleidingsOnderdeelRepository]
 * @property rubricRepository [RubricRepository]
 * @property context [Context]
 * @property uiScope [Job] for co-routine
 * @property coroutineScope Scope for co-routine
 * @property _opleidingsOnderdelen [LiveData] [List] of [OpleidingsOnderdeel]
 * @property gefilterdeOpleidingsOnderdelen Filtered [List] of [OpleidingsOnderdeel]
 * @property _status Private status for [OpleidingsOnderdeel]
 * @property status Public getter for [_status]
 * @property _navigateToRubricSelect Private indicator for navigation in Fragment
 * @property navigateToRubricSelect Public getter for [_navigateToRubricSelect]
 */
class OpleidingsOnderdeelViewModel : ViewModel() {
    /**
     * Properties
     */
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

    private val _navigateToRubricSelect = MutableLiveData<Long>()
    val navigateToRubricSelect
        get() = _navigateToRubricSelect
    /**
     * Constructor. Dependency injectio, initialization of all OpleidingsOnderdelen with a Rubric
     */
    init {
        App.component.inject(this)
        refreshRubricDatabase()
        _opleidingsOnderdelen = opleidingsOnderdeelRepository.getAllOpleidingsOnderdelenWithRubric()
        gefilterdeOpleidingsOnderdelen.addSource(_opleidingsOnderdelen){
            gefilterdeOpleidingsOnderdelen.value = it

        }
    }
    /**
     * Function that filters opleidingsonderdelen from SearchBar input on Fragment
     * @param filterText Input to filter the opleidingsonderdelen
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
        }
    }
    /**
     * Co-Routine for synchronizing all [OpleidingsOnderdeel] and [Rubric] on backend API to Room database.
     *  when Network is available
     * @see OpleidingsOnderdeelRepository
     * @see RubricRepository
     * @see withContext
     * @see Dispatchers.IO
     */
    private fun refreshRubricDatabase() {
        if (isNetworkAvailable(context)) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    opleidingsOnderdeelRepository.refreshOpleidingsOnderdelen()
                    rubricRepository.refreshRubrics()
                }
            }
        }
    }
    /**
     * onClickListener handling when an [OpleidingsOnderdeel] is clicked in Fragment.
     * @param id ID from [OpleidingsOnderdeel]
     */
    fun onOpleidingsOnderdeelClicked(id: Long) {
        _navigateToRubricSelect.value = id
    }
    /**
     * Resetting the navigation control when navigated in the Fragment
     */
    fun onOpleidingsOnderdeelNavigated() {
        _navigateToRubricSelect.value = null
    }
    /**
     * Function that is called when the [LeerlingSelectViewModel] is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}