package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.*
import be.hogent.tile3.rubricapplication.persistence.*
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CriteriumOverzichtViewModel(
    private val rubricId: String,
    private val studentId: Long
) :
    ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var rubricRepository: RubricRepository
    @Inject
    lateinit var niveauRepository: NiveauRepository
    @Inject
    lateinit var criteriumRepository: CriteriumRepository
    @Inject
    lateinit var evaluatieRepository: EvaluatieRepository
    @Inject
    lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _rubricEvaluatie: LiveData<EvaluatieRubric> = liveData {
        coroutineScope.launch {
            val data = getEvaluatieRubric(rubricId)
            _geselecteerdCriterium.value = data.criteria[0]
            _positieGeselecteerdCriterium.value = 0
            var grootteRubricCriteria: Int? = data.criteria.size
            _positieLaatsteCriterium.value =
                if (grootteRubricCriteria == null) 0 else (grootteRubricCriteria - 1)
            emit(data)
        }
    }
    val rubricEvaluatie: LiveData<EvaluatieRubric>
        get() = _rubricEvaluatie

    private suspend fun getEvaluatieRubric(rubricId: String): EvaluatieRubric{
        return withContext(Dispatchers.IO){
            rubricRepository.getEvaluatieRubric(rubricId)
        }
    }

//    private var _huidigeRubric = MediatorLiveData<Rubric>()
//
//    private val _rubricCriteria = MediatorLiveData<List<Criterium>?>()
//    val rubricCriteria: LiveData<List<Criterium>?>
//        get() = _rubricCriteria

    private val _geselecteerdCriterium = MediatorLiveData<Criterium>()
    val geselecteerdCriterium: LiveData<Criterium>
        get() = _geselecteerdCriterium

    private val _positieGeselecteerdCriterium = MediatorLiveData<Int>()
    val positieGeselecteerdCriterium: LiveData<Int>
        get() = _positieGeselecteerdCriterium

    // Variabele wordt gebruikt om performantieredenen; zo moet de grootte van de lijst criteria
    // slechts 1x berekend worden tijdens een evaluatie.
    private val _positieLaatsteCriterium = MediatorLiveData<Int>()
    val positieLaatsteCriterium: LiveData<Int>
        get() = _positieLaatsteCriterium

    private val _overzichtPaneelUitgeklapt = MutableLiveData<Boolean>().apply { postValue(true) }
    val overzichtPaneelUitgeklapt: LiveData<Boolean>
        get() = _overzichtPaneelUitgeklapt

    private val _evaluatie = MutableLiveData<Evaluatie>()
    val evaluatie: LiveData<Evaluatie>
        get() = _evaluatie

    private val _criteriaInitialized = MutableLiveData<Boolean>().apply { postValue(false) }
    val criteriaInitialized: LiveData<Boolean>
        get() = _criteriaInitialized

    private val _persisterenVoltooid = MutableLiveData<Boolean>().apply { postValue(false) }
    val persisterenVoltooid: LiveData<Boolean>
        get() = _persisterenVoltooid


    init {
        Log.i("CriteriumOverzichtVM", "Init-block starts execution")
//        var rubricId = rubricId
        //------------------------------------------------------------------------------------------


        coroutineScope.launch {
//            //            prepareData()
//            _huidigeRubric.addSource(
//                rubricRepository.get(rubricId),
//                _huidigeRubric::setValue
//            )
//            _rubricCriteria.addSource(
//                criteriumRepository.getCriteriaForRubric(rubricId)
//            ) { result: List<Criterium>? ->
//                if (!result.isNullOrEmpty()) {
//                    _rubricCriteria.value = result
//                }
//            }
//            _geselecteerdCriterium.addSource(_rubricCriteria) { result: List<Criterium>? ->
//                result?.let {
//                    _geselecteerdCriterium.value = result[0]
//                    _positieGeselecteerdCriterium.value = 0
//                    var grootteRubricCriteria: Int? = result.size
//                    _positieLaatsteCriterium.value =
//                        if (grootteRubricCriteria == null) 0 else (grootteRubricCriteria - 1)
//                }
//            }


            initialiseerEvaluatie()


        }
        App.component.inject(this)
    }

    private fun prepareData() {
        if (isNetworkAvailable()) {
            coroutineScope.launch {
                //                rubricRepository.refreshRubrics()
//                initialiseerDummyEvaluatie()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun onCriteriumClicked(criteriumId: String, positie: Int) {
        _geselecteerdCriterium.value =
            rubricEvaluatie.value?.criteria?.singleOrNull { it?.criteriumId == criteriumId }
        _positieGeselecteerdCriterium?.value = positie
    }

    fun onUpEdgeButtonClicked() {
        onEdgeButtonClicked(Direction.UP)
    }

    fun onDownEdgeButtonClicked() {
        onEdgeButtonClicked(Direction.DOWN)
    }

    private enum class Direction {
        UP,
        DOWN
    }

    private fun onEdgeButtonClicked(direction: Direction) {
        var oudePositie: Int? = positieGeselecteerdCriterium.value
        var nieuwePositie: Int = if (oudePositie == null) 0 else {
            if (direction == Direction.UP) oudePositie - 1 else oudePositie + 1
        }

        _geselecteerdCriterium.value = rubricEvaluatie.value?.criteria?.get(nieuwePositie)

        _positieGeselecteerdCriterium?.value = nieuwePositie
    }

    fun onKlapInKlapUitButtonClicked() {
        _overzichtPaneelUitgeklapt.value?.let {
            _overzichtPaneelUitgeklapt.value = !it
        }
    }

    fun persisteerEvaluatie() {
        coroutineScope.launch {
            evaluatieRepository.persisteerTemp(_evaluatie.value!!)
            _persisterenVoltooid.value = true
        }
//
    }

    fun onCriteriaInitializedComplete() {
        _criteriaInitialized.value = false
    }

    private fun initialiseerEvaluatie() {
        if (_evaluatie.value == null) {
            coroutineScope.launch {
                _evaluatie.value = withContext(Dispatchers.IO) {
                    evaluatieRepository.verwijderVorigeTempEvaluatie()
                    val bestaandeEvaluatie =
                        evaluatieRepository.getByRubricAndStudent(rubricId, studentId)
                    if (bestaandeEvaluatie == null) {
                        initialiseerNieuweEvaluatie()
                    } else
                        initialiseerBestaandeEvaluatie(bestaandeEvaluatie)
                }
            }
        }

    }

    private suspend fun initialiseerBestaandeEvaluatie(bestaandeEvaluatie: Evaluatie): Evaluatie {
        return withContext(Dispatchers.IO) {
            //Maak de evaluatie
            evaluatieRepository.createTempFromBestaande(bestaandeEvaluatie)

        }
    }

    fun navigatieNaPersisterenVoltooidCompleted(){
        _persisterenVoltooid.value = false
    }

    private suspend fun initialiseerNieuweEvaluatie(): Evaluatie {
        return withContext(Dispatchers.IO) {
            //Maak de evaluatie
            val tempEvaluatie = Evaluatie(TEMP_EVALUATIE_ID, studentId, rubricId)
            evaluatieRepository.insertTemp(tempEvaluatie)

            //Maak de criteriumEvaluaties
            val criteria = criteriumRepository.getCriteriaListForRubric(rubricId)
            val criteriumEvaluaties = ArrayList<CriteriumEvaluatie>()
            criteria?.forEach {
                val niveau = niveauRepository.getNiveausForCriterium(it.criteriumId)
                    .singleOrNull { it.volgnummer == 0 }

                val criteriumEvaluatie = CriteriumEvaluatie(
                    tempEvaluatie.evaluatieId,
                    it.criteriumId,
                    niveau?.niveauId,
                    niveau?.ondergrens,
                    ""
                )
                criteriumEvaluaties.add(criteriumEvaluatie)
            }
            criteriumEvaluatieRepository.insertAllTemp(criteriumEvaluaties)
//            _criteriaInitialized.value = true
            tempEvaluatie

        }
    }

/*    private suspend fun initialiseerDummyEvaluatie() {
        withContext(Dispatchers.IO) {
            evaluatieRepository.insert(Evaluatie("", *//* "1" , *//*1, "1"))
            criteriumEvaluatieRepository.insertAll(
                listOf(
                    CriteriumEvaluatie(
//                        0L,
                        "",
                        "1",
                        3L,
                        null,
                        "LoremIpsumTesterdieTest"
                    ),
                    CriteriumEvaluatie("", "2", 7L, null, "HiHiHiHaHaHa"),
                    CriteriumEvaluatie(
//                        0L,
                        "",
                        "3",
                        9L,
                        null,
                        "SleepDeprivationIsADrug"
                    )
                )
            )
        }
    }*/

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}