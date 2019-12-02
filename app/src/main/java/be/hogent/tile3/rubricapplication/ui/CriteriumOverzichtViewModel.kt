package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.*
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.persistence.*
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class CriteriumOverzichtViewModel(
    private val rubricId: Long,
    val student: Student,
    val docent: Docent
) :
    ViewModel() {

    /* PRIVATE VARIABLES -------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/
    // Context
    @Inject lateinit var context: Context

    // Repositories
    @Inject lateinit var rubricRepository: RubricRepository
    @Inject lateinit var evaluatieRepository: EvaluatieRepository
    @Inject lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository
    @Inject lateinit var studentRepository: StudentRepository
    @Inject lateinit var rubricApi: RubricApi


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

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _totaalScore = MutableLiveData<Int>()
    val totaalScore: LiveData<Int>
        get() = _totaalScore

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

    private suspend fun getEvaluatieRubric(rubricId: Long): EvaluatieRubric{
        return withContext(Dispatchers.IO){
            rubricRepository.getEvaluatieRubric(rubricId)
        }
    }

    private fun initialiseerProperties(data: EvaluatieRubric){
        _score.value = 0
        _totaalScore.value = 0
        _geselecteerdCriterium.value = data.criteria[0]
        _positieGeselecteerdCriterium.value = 0
        val grootteRubricCriteria: Int? = data.criteria.size
        _positieLaatsteCriterium.value =
            if (grootteRubricCriteria == null) 0 else (grootteRubricCriteria - 1)
        initialiseerEvaluatie(data)
        onGeselecteerdCriteriumGewijzigd(data.criteria[0]?.criteriumId, 0, data)
    }
    private fun initialiseerEvaluatie(data: EvaluatieRubric) = runBlocking {

    private fun initialiseerEvaluatie(data: EvaluatieRubric) = runBlocking {

        // 1: nieuwe evaluatie, of bestaande evaluatie?
        var evaluatie: Evaluatie? = geefEvaluatie(true)
        if(evaluatie == null) {
            //Geen temp gevonden
            evaluatie = geefEvaluatie()
        }

        // 2: temp criteriumEvaluaties aanmaken, persisteren & instellen
        var bestaandeCriteriumEvaluaties: MutableList<CriteriumEvaluatie>? = null
        if(evaluatie != null){
            bestaandeCriteriumEvaluaties = geefCriteriumEvaluaties(evaluatie.evaluatieId)?.toMutableList()
        }
        val criteriumEvaluaties = ArrayList<CriteriumEvaluatie>()
        data.criteria.forEach{
            val criteriumEvaluatie: CriteriumEvaluatie? = bestaandeCriteriumEvaluaties?.singleOrNull{
                    critEval -> it.criteriumId == critEval.criteriumId
            }
            val minNiveau: Niveau? = data.niveausCriteria.singleOrNull{niv -> niv.criteriumId == it.criteriumId && niv.volgnummer == 0}
            val nieuweCriteriumEvaluatie = CriteriumEvaluatie(
                TEMP_EVALUATIE_ID,
                it.criteriumId,
                criteriumEvaluatie?.behaaldNiveau ?: minNiveau?.niveauId,
                criteriumEvaluatie?.score ?: minNiveau?.ondergrens,
                criteriumEvaluatie?.commentaar ?: ""
            )
            criteriumEvaluaties.add(nieuweCriteriumEvaluatie)
            bestaandeCriteriumEvaluaties?.remove(criteriumEvaluatie)
            nieuweCriteriumEvaluatie.score?.let{
                _score.value = _score.value!!.plus(it)
            }
            _totaalScore.value = _totaalScore.value!!.plus(data.niveausCriteria.last().bovengrens)
            bestaandeCriteriumEvaluaties?.removeAt(0)
        }
        // 3: persisteren
        if(evaluatie?.evaluatieId != TEMP_EVALUATIE_ID){
            //Geen nieuwe temp opslaan als er al een temp aanwezig is (indien app gestopt/gesloten is)
            slaTempEvaluatieOp(Evaluatie(TEMP_EVALUATIE_ID, student.studentId, rubricId, docent.docentId))
        }
        slaTempCriteriumEvaluatiesOp(criteriumEvaluaties)
    }


    private suspend fun geefEvaluatie(temp: Boolean = false): Evaluatie? {
        return withContext(Dispatchers.IO){
            evaluatieRepository.getByRubricAndStudent(rubricId, student.studentId, temp)
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

    fun onGeselecteerdCriteriumGewijzigd(criteriumId: Long, positie: Int, data: EvaluatieRubric? = null) {
        _geselecteerdCriterium.value =
            if(data != null) data.criteria?.singleOrNull { it.criteriumId == criteriumId }
            else evaluatieRubric.value?.criteria?.singleOrNull { it.criteriumId == criteriumId }

        _positieGeselecteerdCriterium.value = positie

        _criteriumEvaluatie.value = criteriumEvaluaties.value?.singleOrNull {
            it?.criteriumId == criteriumId }

        _criteriumNiveaus.value =
            data?.niveausCriteria?.filter{it.criteriumId == criteriumId}?.sortedBy{it.volgnummer}
                ?: evaluatieRubric.value?.niveausCriteria
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

        if(niveauId != geselecteerdCriteriumNiveau.value?.niveauId){
            _criteriumEvaluatie.value?.score?.let{
                _score.value = _score.value!!.minus(it)
            }
        }
        _geselecteerdCriteriumNiveau.value = criteriumNiveaus.value?.singleOrNull{it.niveauId == niveauId}
        _positieGeselecteerdCriteriumNiveau?.value = positie
        _criteriumEvaluatie.value?.behaaldNiveau = niveauId
        _criteriumEvaluatie.value?.score = 0 //geselecteerdCriteriumNiveau.value?.ondergrens ?: 0
        Log.i("TestCriteriumOverzicht", "Evaluatie wordt gepersisteerd")
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
    }

    fun onScoreChanged(oudeScore: Int, nieuweScore: Int){
        _score.value = _score.value!!.plus(nieuweScore).minus(_criteriumEvaluatie.value?.score!!)
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
        val oudePositie: Int? = positieGeselecteerdCriterium.value
        val nieuwePositie: Int = if (oudePositie == null) 0 else {
            if (direction == Direction.UP) oudePositie - 1 else oudePositie + 1
        }
        _geselecteerdCriterium.value = evaluatieRubric.value?.criteria?.get(nieuwePositie)
        _positieGeselecteerdCriterium.value = nieuwePositie
        onGeselecteerdCriteriumGewijzigd(geselecteerdCriterium.value?.criteriumId ?: 0, nieuwePositie)
    }

    fun onKlapInKlapUitButtonClicked() {
        _overzichtPaneelUitgeklapt.value?.let {
            _overzichtPaneelUitgeklapt.value = !it
        }
    }




    /* PERSISTENTIE, BREAKDOWN -------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    private fun persisteerCriteriumEvaluatie(obj: CriteriumEvaluatie?){
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

    fun deleteTempEvaluatie() {
        coroutineScope.launch {
            evaluatieRepository.verwijderVorigeTempEvaluatie()
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