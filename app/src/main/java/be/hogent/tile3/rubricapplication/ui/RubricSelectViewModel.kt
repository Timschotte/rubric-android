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
import java.util.*
import javax.inject.Inject
/**
 * ViewModel for RubricSelect
 * @constructor Creates a [RubricSelectViewModel]
 * @property opleidingsOnderdeelRepository [OpleidingsOnderdeelRepository]
 * @property rubricRepository [RubricRepository]
 * @property context [Context]
 * @property _rubrics Private [LiveData] [List] of [Rubric]
 * @property gefilterdeRubrics Public getter for filtered [LiveData] [List] of [Rubric]
 * @property opleidingsOnderdeel Current [OpleidingsOnderdeel]
 * @property _navigateToKlasSelect Private indicator for navigation in Fragment
 * @property navigateToKlasSelect Public getter for [_navigateToKlasSelect]
 */
class RubricSelectViewModel(opleidingsOnderdeelId: Long) : ViewModel() {
    /**
     * Properties
     */
    @Inject
    lateinit var rubricRepository: RubricRepository
    @Inject
    lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository
    @Inject
    lateinit var context: Context

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _rubrics: LiveData<List<Rubric>>
    val gefilterdeRubrics = MediatorLiveData<List<Rubric>>()

    private val _navigateToKlasSelect = MutableLiveData<Long>()
    val navigateToKlasSelect
        get() = _navigateToKlasSelect

    val opleidingsOnderdeel = MediatorLiveData<OpleidingsOnderdeel>()

    private val _refreshIsComplete = MutableLiveData<Boolean>(false)
    val refreshIsComplete: LiveData<Boolean>
        get() = _refreshIsComplete

    /**
     * Constructor. Dependency injection, initializing current opleidingsonderdeel object and all rubrics
     */
    init {
        App.component.inject(this)
        refreshRubricDatabase()
        opleidingsOnderdeel.addSource(
            opleidingsOnderdeelRepository.get(opleidingsOnderdeelId),
            opleidingsOnderdeel::setValue
        )
        _rubrics = rubricRepository.getAllRubricsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        gefilterdeRubrics.addSource(_rubrics) {
            gefilterdeRubrics.value = it
        }
    }
    private fun refreshRubricDatabase(){
        if (isNetworkAvailable(context)) {
            uiScope.launch {
                withContext(Dispatchers.IO) {
                    rubricRepository.refreshRubrics()
                }.apply {
                    _refreshIsComplete.value = true
                }
            }
        }
    }
    /**
     * Function that filters rubrics from SearchBar input on Fragment
     * @param filterText Input to filter the rubrics
     */
    fun filterChanged(filterText: String?) {
        if (filterText != null) {
            _rubrics.value?.let {
                gefilterdeRubrics.removeSource(_rubrics)
                gefilterdeRubrics.addSource(_rubrics) {
                    gefilterdeRubrics.value = it.filter { rubric ->
                        rubric.onderwerp?.toLowerCase(Locale.FRENCH).orEmpty()
                            .contains(filterText.toLowerCase(Locale.FRENCH))
                    }
                }
            }
        }
    }
    /**
     * onClickListener handling when a [Rubric] is clicked in Fragment.
     * @param id ID from [Rubric]
     */
    fun onRubricClicked(id: Long) {
        _navigateToKlasSelect.value = id
    }
    /**
     * Resetting the navigation control when navigated in the Fragment
     */
    fun onOpleidingsOnderdeelNavigated() {
        _navigateToKlasSelect.value = null
    }

}