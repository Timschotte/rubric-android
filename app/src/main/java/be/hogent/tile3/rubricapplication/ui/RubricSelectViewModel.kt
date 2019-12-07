package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.OpleidingsOnderdeelRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import java.util.*
import javax.inject.Inject

class RubricSelectViewModel(opleidingsOnderdeelId: Long) : ViewModel() {

    @Inject
    lateinit var rubricRepository: RubricRepository

    @Inject
    lateinit var opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository

    @Inject
    lateinit var context: Context

    private val _rubrics: LiveData<List<Rubric>>
    val gefilterdeRubrics = MediatorLiveData<List<Rubric>>()

    val opleidingsOnderdeel = MediatorLiveData<OpleidingsOnderdeel>()

    init {
        App.component.inject(this)
        opleidingsOnderdeel.addSource(
            opleidingsOnderdeelRepository.get(opleidingsOnderdeelId),
            opleidingsOnderdeel::setValue
        )
        _rubrics = rubricRepository.getAllRubricsFromOpleidingsOnderdeel(opleidingsOnderdeelId)
        gefilterdeRubrics.addSource(_rubrics) {
            gefilterdeRubrics.value = it
        }
    }

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

    private val _navigateToKlasSelect = MutableLiveData<Long>()
    val navigateToKlasSelect
        get() = _navigateToKlasSelect


    fun onRubricClicked(id: Long) {
        _navigateToKlasSelect.value = id
    }

    fun onOpleidingsOnderdeelNavigated() {
        _navigateToKlasSelect.value = null
    }

}