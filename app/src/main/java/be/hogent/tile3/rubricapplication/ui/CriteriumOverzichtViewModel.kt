package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.*
import be.hogent.tile3.rubricapplication.persistence.*
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CriteriumOverzichtViewModel(
    private val rubricId: String,
    private val studentId: Long
) :
    ViewModel() {

    /* PRIVATE VARIABLES -------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    // Context
    @Inject lateinit var context: Context

    // Repositories
    @Inject lateinit var rubricRepository: RubricRepository
    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    @Inject lateinit var evaluatieRepository: EvaluatieRepository
    @Inject lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository

    // Coroutine variables
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)




    /* BACKED PROPERTIES -------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    // DOMAIN
    private val _evaluatieRubric: LiveData<EvaluatieRubric> = liveData {
        coroutineScope.launch {
            val data = getEvaluatieRubric(rubricId)
            initialiseerProperties(data)
            emit(data)
        }
    }
    val evaluatieRubric: LiveData<EvaluatieRubric>
        get() = _evaluatieRubric

    private val _evaluatie = MutableLiveData<Evaluatie>()
    val evaluatie: LiveData<Evaluatie>
        get() = _evaluatie

    private val _criteriumEvaluaties = MutableLiveData<List<CriteriumEvaluatie>>()
    val criteriumEvaluaties: LiveData<List<CriteriumEvaluatie>>
        get() = _criteriumEvaluaties

    // DETAILPANEL
    private val _geselecteerdCriterium = MediatorLiveData<Criterium>()
    val geselecteerdCriterium: LiveData<Criterium>
        get() = _geselecteerdCriterium

    private val _criteriumNiveaus = MutableLiveData<List<Niveau>>()
    val criteriumNiveaus: LiveData<List<Niveau>>
        get() = _criteriumNiveaus

    private val _criteriumEvaluatie = MutableLiveData<CriteriumEvaluatie>()
    val criteriumEvaluatie: LiveData<CriteriumEvaluatie>
        get() = _criteriumEvaluatie

    // UI OVERZICHTPANEL
    private val _positieGeselecteerdCriterium = MediatorLiveData<Int>()
    val positieGeselecteerdCriterium: LiveData<Int>
        get() = _positieGeselecteerdCriterium

    private val _positieLaatsteCriterium = MediatorLiveData<Int>()
    val positieLaatsteCriterium: LiveData<Int>
        get() = _positieLaatsteCriterium

    private val _overzichtPaneelUitgeklapt = MutableLiveData<Boolean>().apply { postValue(true) }
    val overzichtPaneelUitgeklapt: LiveData<Boolean>
        get() = _overzichtPaneelUitgeklapt

    // UI DETAILPANEL
    private val _geselecteerdCriteriumNiveau = MutableLiveData<Niveau>()
    val geselecteerdCriteriumNiveau: LiveData<Niveau>
        get() = _geselecteerdCriteriumNiveau

    private val _positieGeselecteerdCriteriumNiveau = MutableLiveData<Int>()
    val positieGeselecteerdCriteriumNiveau: LiveData<Int>
        get() = _positieGeselecteerdCriteriumNiveau

    // PERSISTENTIE
    private val _persisterenVoltooid = MutableLiveData<Boolean>().apply { postValue(false) }
    val persisterenVoltooid: LiveData<Boolean>
        get() = _persisterenVoltooid




    /* INITIALIZATION ----------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/
    init {
        App.component.inject(this)
    }

    private suspend fun getEvaluatieRubric(rubricId: String): EvaluatieRubric{
        return withContext(Dispatchers.IO){
            rubricRepository.getEvaluatieRubric(rubricId)
        }
    }

    private fun initialiseerProperties(data: EvaluatieRubric){
        _geselecteerdCriterium.value = data.criteria[0]
        _positieGeselecteerdCriterium.value = 0
        var grootteRubricCriteria: Int? = data.criteria.size
        _positieLaatsteCriterium.value =
            if (grootteRubricCriteria == null) 0 else (grootteRubricCriteria - 1)
        initialiseerEvaluatie(data)
        onGeselecteerdCriteriumGewijzigd(data.criteria[0]?.criteriumId, 0, data)
    }

    private fun initialiseerEvaluatie(data: EvaluatieRubric) = runBlocking {
        Log.i("Test4", "Data Criteria: ")
        data.criteria?.forEach{
            Log.i("Test4", it.toString())
        }
        // 1: nieuwe evaluatie, of bestaande evaluatie?
        var evaluatie: Evaluatie? = geefEvaluatie(rubricId, studentId)
        // 2: temp evaluatie aanmaken
        slaTempEvaluatieOp(Evaluatie(TEMP_EVALUATIE_ID, studentId, rubricId))
        // 3: temp criteriumEvaluaties aanmaken, persisteren & instellen
        var bestaandeCriteriumEvaluaties: MutableList<CriteriumEvaluatie>? = null
        if(evaluatie != null){
            bestaandeCriteriumEvaluaties = geefCriteriumEvaluaties(evaluatie?.evaluatieId)?.toMutableList()
            Log.i("Test4", "Bestaande CriteriumEvaluaties: ")
            bestaandeCriteriumEvaluaties?.forEach{
                Log.i("Test4", it.toString())
            }
        }
        var criteriumEvaluaties = ArrayList<CriteriumEvaluatie>()
        data.criteria.forEach{
            var criteriumEvaluatie: CriteriumEvaluatie? = bestaandeCriteriumEvaluaties?.singleOrNull{
                    critEval -> it.criteriumId == critEval.criteriumId
            }
            var minNiveau: Niveau? = data.niveausCriteria.singleOrNull{niv -> niv.criteriumId == it.criteriumId && niv.volgnummer == 0}
            var nieuweCriteriumEvaluatie = CriteriumEvaluatie(
                TEMP_EVALUATIE_ID,
                it.criteriumId,
                criteriumEvaluatie?.behaaldNiveau ?: minNiveau?.niveauId,
                criteriumEvaluatie?.score ?: minNiveau?.ondergrens,
                criteriumEvaluatie?.commentaar ?: ""
            )
            Log.i("test4", "Nieuwe CriteriumEvaluatie: " + nieuweCriteriumEvaluatie.toString())
            criteriumEvaluaties.add(nieuweCriteriumEvaluatie)
            bestaandeCriteriumEvaluaties?.removeAt(0)
        }
        Log.i("Test4", "Nieuwe/Temp CriteriumEvaluaties: ")
        criteriumEvaluaties?.forEach{
            Log.i("Test4", it.toString())
        }
        // 4: temp criteriumEvaluaties persisteren & instellen
        slaTempCriteriumEvaluatiesOp(criteriumEvaluaties)
    }

    private suspend fun geefEvaluatie(rubricId: String, studentId: Long): Evaluatie? {
        return withContext(Dispatchers.IO){
            evaluatieRepository.getByRubricAndStudent(rubricId, studentId)
        }
    }

    private suspend fun geefCriteriumEvaluaties(evaluatieId: String?): List<CriteriumEvaluatie>? {
        return withContext(Dispatchers.IO){
            if(evaluatieId != null)
                criteriumEvaluatieRepository.getAllForEvaluatie(evaluatieId)
            else
                null
        }
    }

    private suspend fun slaTempEvaluatieOp(obj: Evaluatie){
        _evaluatie.value = obj
        withContext(Dispatchers.IO){
            evaluatieRepository.insert(obj)
        }
    }

    private suspend fun slaTempCriteriumEvaluatiesOp(obj: List<CriteriumEvaluatie>){
        _criteriumEvaluaties.value = obj
        withContext(Dispatchers.IO){
            criteriumEvaluatieRepository.insertAll(obj)
        }
    }




    /* CLICK HANDLERS ----------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    fun onGeselecteerdCriteriumGewijzigd(criteriumId: String, positie: Int, data: EvaluatieRubric? = null) {
        _geselecteerdCriterium.value =
            if(data != null) data.criteria?.singleOrNull { it?.criteriumId == criteriumId }
            else evaluatieRubric.value?.criteria?.singleOrNull { it?.criteriumId == criteriumId }

        _positieGeselecteerdCriterium?.value = positie

        _criteriumEvaluatie.value = criteriumEvaluaties.value?.singleOrNull {
            it?.criteriumId == criteriumId }

        _criteriumNiveaus.value =
            if(data != null) data.niveausCriteria
                ?.filter{it.criteriumId == criteriumId}
                ?.sortedBy{it.volgnummer}
            else evaluatieRubric.value?.niveausCriteria
                ?.filter{it.criteriumId == criteriumId}
                ?.sortedBy{it.volgnummer}

        _geselecteerdCriteriumNiveau.value =
            if(data != null) data.niveausCriteria?.singleOrNull{
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau}
            else evaluatieRubric.value?.niveausCriteria?.singleOrNull{
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau}

        _positieGeselecteerdCriteriumNiveau.value =
            criteriumNiveaus.value?.indexOf(criteriumNiveaus.value?.singleOrNull {
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
            })
    }

    fun onNiveauClicked(niveauId: Long, positie: Int){
        _geselecteerdCriteriumNiveau.value = criteriumNiveaus.value?.singleOrNull{it.niveauId == niveauId}
        _positieGeselecteerdCriteriumNiveau?.value = positie
        _criteriumEvaluatie.value?.behaaldNiveau = niveauId
        _criteriumEvaluatie.value?.score = geselecteerdCriteriumNiveau.value?.ondergrens ?: 0
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
    }

    fun onScoreChanged(oudeScore: Int, nieuweScore: Int){
        _criteriumEvaluatie.value?.score = nieuweScore
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
    }

    fun onCommentaarChanged(oudeCommentaar: String, nieuweCommentaar: String){
        _criteriumEvaluatie.value?.commentaar = nieuweCommentaar
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
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
        _geselecteerdCriterium.value = evaluatieRubric.value?.criteria?.get(nieuwePositie)
        _positieGeselecteerdCriterium?.value = nieuwePositie
        onGeselecteerdCriteriumGewijzigd(geselecteerdCriterium.value?.criteriumId ?: "", nieuwePositie)
    }

    fun onKlapInKlapUitButtonClicked() {
        _overzichtPaneelUitgeklapt.value?.let {
            _overzichtPaneelUitgeklapt.value = !it
        }
    }




    /* PERSISTENTIE, BREAKDOWN -------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    fun persisteerCriteriumEvaluatie(obj: CriteriumEvaluatie?){
        coroutineScope.launch{
            if(obj != null)
                criteriumEvaluatieRepository.update(obj)
        }
    }

    fun persisteerEvaluatie() {
        coroutineScope.launch {
            evaluatieRepository.persisteerTemp(_evaluatie.value!!)
            _persisterenVoltooid.value = true
        }
    }

    fun navigatieNaPersisterenVoltooidCompleted(){
        _persisterenVoltooid.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}