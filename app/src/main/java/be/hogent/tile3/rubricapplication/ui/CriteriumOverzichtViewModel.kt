package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.*
import androidx.work.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.R
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.*
import be.hogent.tile3.rubricapplication.persistence.CriteriumEvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.EvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import be.hogent.tile3.rubricapplication.utils.isNetworkAvailable
import be.hogent.tile3.rubricapplication.workers.NetworkPersistenceWorker
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * ViewModel for CriteriumOverzicht
 * @constructor Creates a [CriteriumOverzichtViewModel]
 * @property rubricId ID for [Rubric]
 * @property student [Student]
 * @property context [Context]
 * @property rubricRepository [RubricRepository]
 * @property evaluatieRepository [EvaluatieRepository]
 * @property criteriumEvaluatie [CriteriumEvaluatieRepository]
 * @property viewModelJob [Job] for co-routine
 * @property uiScope UI scope for co-routine
 * @property ioScope IO scope for co-routine
 * @property _evaluatieRubric Private [EvaluatieRubric] for a [Rubric] retrieved from Room database
 * @property evaluatieRubric Public getter for [_evaluatieRubric]
 * @property _evaluatie Private [Evaluatie]
 * @property evaluatie Public getter for [_evaluatie]
 * @property _score Private score for a [Criterium]
 * @property score Public getter for [_score]
 * @property _totaalScore Private total-score for a [Criterium]
 * @property _totaalScore Public getter for [_totaalScore]
 * @property _criteriumEvaluaties Private [List] of [CriteriumEvaluatie] for a [Criterium]
 * @property criteriumEvaluaties Public getter for [_criteriumEvaluaties]
 * @property _geselecteerdCriterium Private [Criterium] for indicating the current selected [Criterium]
 * @property geselecteerdCriterium Public getter for [_geselecteerdCriterium]
 * @property _criteriumNiveaus Private [List] of Niveaus for a [Criterium]
 * @property criteriumNiveaus Public getter for [criteriumNiveaus]
 * @property _criteriumEvaluatie Private [CriteriumEvaluatie] for a  [Criterium]
 * @property criteriumEvaluatie Public getter for [_criteriumEvaluatie]
 * @property _positieGeselecteerdCriterium Private position of the selected [Criterium]
 * @property positieGeselecteerdCriterium Public getter for [_positieGeselecteerdCriterium]
 * @property _positieLaatsteCriterium Private position of the last [Criterium]
 * @property positieLaatsteCriterium Public getter for [_positieLaatsteCriterium]
 * @property _overzichtPaneelUitgeklapt Private indicator if the overviewpanel is open in Fragment
 * @property overzichtPaneelUitgeklapt Public getter for [_overzichtPaneelUitgeklapt]
 * @property _geselecteerdCriteriumNiveau Private [Niveau] for indicating the current selected [Niveau]
 * @property geselecteerdCriteriumNiveau Public getter for [_geselecteerdCriteriumNiveau]
 * @property _positieGeselecteerdCriteriumNiveau Private position of the selected [Niveau]
 * @property positieGeselecteerdCriteriumNiveau Public getter for [_positieGeselecteerdCriteriumNiveau]
 * @property _persisterenVoltooid Private indicator for indicating if persisting is complete
 * @property persisterenVoltooid Public getter for [_persisterenVoltooid]
 * @see [ViewModel]
 */
class CriteriumOverzichtViewModel(
    private val rubricId: Long,
    val student: Student
) :
    ViewModel() {

    /**
     * Properties
     */
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var rubricRepository: RubricRepository
    @Inject
    lateinit var evaluatieRepository: EvaluatieRepository
    @Inject
    lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository
    /**
     * Co-Routine properties
     */
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val ioScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    /**
     * Backend properties
     */
    private val _evaluatieRubric: LiveData<EvaluatieRubric> = liveData {
        uiScope.launch {
            val data = getEvaluatieRubric(rubricId)
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

    /**
     * Constructor.
     * Dagger dependency injection and initializing _score and _totalscore
     */
    init {
        App.component.inject(this)
        _score.value = 0
        _totaalScore.value = 0
    }
    /**
     * Co-Routine for retrieving a [EvaluatieRubric] for a given [Rubric] from Room database.
     * @param rubricId ID for a given [Rubric]
     * @return [EvaluatieRubric]
     * @see RubricRepository
     */
    private suspend fun getEvaluatieRubric(rubricId: Long): EvaluatieRubric {
        return withContext(Dispatchers.IO) {
            rubricRepository.getEvaluatieRubric(rubricId)
        }
    }

    /**
     * Function that initializes [EvaluatieRubric] properties
     * @param data [EvaluatieRubric]
     */
    private fun initialiseerProperties(data: EvaluatieRubric){
        _geselecteerdCriterium.value = data.criteria[0]
        _positieGeselecteerdCriterium.value = 0
        val grootteRubricCriteria: Int? = data.criteria.size
        _positieLaatsteCriterium.value =
            if (grootteRubricCriteria == null) 0 else (grootteRubricCriteria - 1)
    }

    /**
     * Function that initializes an [Evaluatie]. Checks if the current [EvaluatieRubric] is a temporary Evaluation or not.
     * Recreates the existing [Evaluatie] and persists as a temporary [Evaluatie]. Initializes UI properties.
     */
    fun initialiseerEvaluatie() = runBlocking {
        val data = _evaluatieRubric.value!!
        // 1: nieuwe evaluatie, of bestaande evaluatie?
        var evaluatie: Evaluatie? = geefEvaluatie(true)
        if (evaluatie?.evaluatieId.isNullOrEmpty()) {
            //Geen temp gevonden
            evaluatie = geefEvaluatie()
        }

        // 2: temp criteriumEvaluaties aanmaken, persisteren & instellen
        val bestaandeCriteriumEvaluaties: MutableList<CriteriumEvaluatie> = mutableListOf()
        evaluatie?.let {
            if (it.evaluatieId.isNotEmpty()) {
                bestaandeCriteriumEvaluaties.addAll(geefCriteriumEvaluaties(it.evaluatieId))
            }
        }
        val criteriumEvaluaties = ArrayList<CriteriumEvaluatie>()
        _evaluatieRubric.value!!.criteria.forEach { criterium ->
            val criteriumEvaluatie: CriteriumEvaluatie? =
                bestaandeCriteriumEvaluaties?.singleOrNull { critEval ->
                    criterium.criteriumId == critEval.criteriumId
                }

            val minNiveau: Niveau? =
                data.niveausCriteria.singleOrNull { niv -> niv.criteriumId == criterium.criteriumId && niv.volgnummer == 0 }
            val nieuweCriteriumEvaluatie = CriteriumEvaluatie(
                TEMP_EVALUATIE_ID,
                criterium.criteriumId,
                criteriumEvaluatie?.behaaldNiveau ?: minNiveau?.niveauId ?: 0,
                criteriumEvaluatie?.score ?: minNiveau?.ondergrens ?: 0,
                criteriumEvaluatie?.commentaar ?: ""
            )
            criteriumEvaluaties.add(nieuweCriteriumEvaluatie)

            bestaandeCriteriumEvaluaties?.remove(criteriumEvaluatie)
            nieuweCriteriumEvaluatie.score?.let{
                _score.value = _score.value?.plus(it)
            }
            _totaalScore.value = _totaalScore.value?.plus(data.niveausCriteria.last().bovengrens)
        }
        // 3: persisteren
        if (evaluatie?.evaluatieId != TEMP_EVALUATIE_ID) {
            //Geen nieuwe temp opslaan als er al een temp aanwezig is (indien app gestopt/gesloten is)
            val temp = Evaluatie(TEMP_EVALUATIE_ID, student.studentId, rubricId, 1, false)
            slaTempEvaluatieOp(temp)
            slaTempCriteriumEvaluatiesOp(criteriumEvaluaties)
        }

        // 4: View goed zetten

        initialiseerProperties(data)
        val currentCriterium =
            criteriumEvaluaties.singleOrNull { c -> c.criteriumId == _geselecteerdCriterium.value?.criteriumId }
        _geselecteerdCriteriumNiveau.value =
            criteriumNiveaus.value?.singleOrNull { it.niveauId == currentCriterium?.behaaldNiveau }
        _positieGeselecteerdCriteriumNiveau?.value = 0
        _criteriumEvaluatie.value?.behaaldNiveau = _geselecteerdCriteriumNiveau.value?.niveauId!!

        if (criteriumEvaluatie.value?.score ?: 0 < _geselecteerdCriteriumNiveau.value?.ondergrens ?: 0) {
            criteriumEvaluatie.value?.score = _geselecteerdCriteriumNiveau.value?.ondergrens ?: 0
        }
        if (criteriumEvaluatie.value?.score ?: 0 > _geselecteerdCriteriumNiveau.value?.bovengrens ?: 0) {
            criteriumEvaluatie.value?.score = _geselecteerdCriteriumNiveau.value?.bovengrens ?: 0
        }
        onGeselecteerdCriteriumGewijzigd(_geselecteerdCriterium?.value!!.criteriumId, 0, data)
    }

    /**
     * Function for retrieving an [Evaluatie] for a given [Rubric] and [Student] from Room database.
     * @param temp
     * @return [Evaluatie]
     * @see EvaluatieRepository
     */
    private fun geefEvaluatie(temp: Boolean = false): Evaluatie? = runBlocking{
        return@runBlocking evaluatieRepository.getByRubricAndStudent(rubricId, student.studentId, temp)
    }
    /**
     * Co-Routine for retrieving all [CriteriumEvaluatie] from a given [Evaluatie] from Room database.
     * This method runs on the IO thread as a background task
     * @param evaluatieId ID from [Evaluatie] to be retrieved
     * @return [List] of [CriteriumEvaluatie]
     * @see CriteriumEvaluatieRepository
     * @see withContext
     * @see Dispatchers.IO
     */
    private suspend fun geefCriteriumEvaluaties(evaluatieId: String?): List<CriteriumEvaluatie> {
        return withContext(Dispatchers.IO) {
            if (!evaluatieId.isNullOrEmpty())
                criteriumEvaluatieRepository.getAllForEvaluatie(evaluatieId)
            else
                emptyList()
        }
    }
    /**
     * Co-Routine for inserting an [Evaluatie] in Room database.
     * This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be inserted
     * @see EvaluatieRepository
     * @see withContext
     * @see Dispatchers.IO
     */
    private suspend fun slaTempEvaluatieOp(evaluatie: Evaluatie) {
        _evaluatie.value = evaluatie
        withContext(Dispatchers.IO) {
            evaluatieRepository.insert(evaluatie)
        }
    }
    /**
     * Co-Routine for inserting a list of [CriteriumEvaluatie] in Room database.
     * This method runs on the IO thread as a background task
     * @param criteriumEvaluaties [List] of [CriteriumEvaluatie] to be inserted
     * @see CriteriumEvaluatieRepository
     * @see withContext
     * @see Dispatchers.IO
     */
    private suspend fun slaTempCriteriumEvaluatiesOp(criteriumEvaluaties: List<CriteriumEvaluatie>) {
        _criteriumEvaluaties.value = criteriumEvaluaties
        withContext(Dispatchers.IO) {
            criteriumEvaluatieRepository.insertAll(criteriumEvaluaties)
        }
    }
    /**
     * onClickListener handling when a [Criterium] is clicked in Fragment.
     * @param criteriumId ID from the selected [Criterium]
     * @param positie Position of the selected [Criterium]
     * @param data [EvaluatieRubric]
     */
    fun onGeselecteerdCriteriumGewijzigd(criteriumId: Long, positie: Int, data: EvaluatieRubric? = null) {
        _geselecteerdCriterium.value =
            if (data != null) data.criteria?.singleOrNull { it.criteriumId == criteriumId }
            else evaluatieRubric.value?.criteria?.singleOrNull { it.criteriumId == criteriumId }

        _positieGeselecteerdCriterium.value = positie

        _criteriumEvaluatie.value = criteriumEvaluaties.value?.singleOrNull {
            it?.criteriumId == criteriumId
        }

        _criteriumNiveaus.value =
            data?.niveausCriteria?.filter { it.criteriumId == criteriumId }?.sortedBy { it.volgnummer }
                ?: evaluatieRubric.value?.niveausCriteria
                    ?.filter { it.criteriumId == criteriumId }
                    ?.sortedBy { it.volgnummer }

        _geselecteerdCriteriumNiveau.value =
            if (data != null) data.niveausCriteria?.singleOrNull {
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
            }
            else evaluatieRubric.value?.niveausCriteria?.singleOrNull {
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
            }

        _positieGeselecteerdCriteriumNiveau.value =
            criteriumNiveaus.value?.indexOf(criteriumNiveaus.value?.singleOrNull {
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
            })
    }
    /**
     * onClickListener handling when a [Niveau] is clicked in Fragment.
     * @param niveauId ID from the selected [Niveau]
     * @param positie Position of the selected [Criterium]
     */
    fun onNiveauClicked(niveauId: Long, positie: Int){

        if(niveauId != geselecteerdCriteriumNiveau.value?.niveauId){
            _criteriumEvaluatie.value?.score?.let{
                _score.value = _score.value!!.minus(it)
            }
        }
        _geselecteerdCriteriumNiveau.value = criteriumNiveaus.value?.singleOrNull{it.niveauId == niveauId}
        _positieGeselecteerdCriteriumNiveau?.value = positie
        _criteriumEvaluatie.value?.behaaldNiveau = niveauId

        //Validate selected score before persisting
        /*
        if (criteriumEvaluatie.value?.score ?: 0 < _geselecteerdCriteriumNiveau.value?.ondergrens ?: 0) {
            criteriumEvaluatie.value?.score = _geselecteerdCriteriumNiveau.value?.ondergrens ?: 0
        }
        if (criteriumEvaluatie.value?.score ?: 0 > _geselecteerdCriteriumNiveau.value?.bovengrens ?: 0) {
            criteriumEvaluatie.value?.score = _geselecteerdCriteriumNiveau.value?.bovengrens ?: 0
        }
        */
        _criteriumEvaluatie.value?.score = geselecteerdCriteriumNiveau.value!!.ondergrens
        _score.value = _score.value!!.plus(geselecteerdCriteriumNiveau.value!!.ondergrens)
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
    }
    /**
     * onClickListener handling when a score (Chip) is clicked for the [CriteriumEvaluatie] in Fragment.
     * @param nieuweScore Value of the selected score
     */
    fun onScoreChanged(nieuweScore: Int){
        _score.value = _score.value!!.plus(nieuweScore).minus(_criteriumEvaluatie.value?.score!!)
        _criteriumEvaluatie.value?.score = nieuweScore
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
    }
    /**
     * onClickListener handling when a comment is added to the [CriteriumEvaluatie] in Fragment.
     * @param nieuweCommentaar Value of comment input from Fragment
     */
    fun onCommentaarChanged(nieuweCommentaar: String) {
        _criteriumEvaluatie.value?.commentaar = nieuweCommentaar
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
        _criteriumEvaluatie.value = _criteriumEvaluatie.value
    }
    /**
     * onClickListener handling when the UP-edge button is clicked in Fragment.
     */
    fun onUpEdgeButtonClicked() {
        onEdgeButtonClicked(Direction.UP)
    }
    /**
     * onClickListener handling when the Down-edge button is clicked in Fragment.
     */
    fun onDownEdgeButtonClicked() {
        onEdgeButtonClicked(Direction.DOWN)
    }
    /**
     * Enum Class for defining navigational directions for the Fragment
     * @property UP
     * @property DOWN
     */
    private enum class Direction {
        UP,
        DOWN
    }
    /**
     * Generic function for handling the edge button navigation in the fragment.
     * @param direction [Direction] for navigation in the fragment
     */
    private fun onEdgeButtonClicked(direction: Direction) {
        val oudePositie: Int? = positieGeselecteerdCriterium.value
        val nieuwePositie: Int = if (oudePositie == null) 0 else {
            if (direction == Direction.UP) oudePositie - 1 else oudePositie + 1
        }
        _geselecteerdCriterium.value = evaluatieRubric.value?.criteria?.get(nieuwePositie)
        _positieGeselecteerdCriterium.value = nieuwePositie
        onGeselecteerdCriteriumGewijzigd(
            geselecteerdCriterium.value?.criteriumId ?: 0,
            nieuwePositie
        )
    }
    /**
     * onClickListener handling when the Overview collapse button is clicked in Fragment.
     */
    fun onKlapInKlapUitButtonClicked() {
        _overzichtPaneelUitgeklapt.value?.let {
            _overzichtPaneelUitgeklapt.value = !it
        }
    }
    /**
     * Co-Routine launcher for updating a [CriteriumEvaluatie] in Room database.
     * This method runs on the IO thread as a background task
     * @param obj [CriteriumEvaluatie] to be updated
     * @see CriteriumEvaluatieRepository
     */
    private fun persisteerCriteriumEvaluatie(obj: CriteriumEvaluatie?) {
        ioScope.launch {
            if (obj != null) {
                criteriumEvaluatieRepository.update(obj)
            }
        }
    }
    /**
     * Co-Routine launcher for persisting a [Evaluatie] in Room database and API backend
     * This method runs on the IO thread as a background task
     * @see EvaluatieRepository
     */
    fun persisteerEvaluatie() {
        uiScope.launch {
            val progress = ContentLoadingProgressBar(context)
            progress.show()
            evaluatieRepository.persisteerTemp(_evaluatie.value!!)
            if (isNetworkAvailable(context)) {
                if (evaluatieRepository.persistEvaluationToNetwork(_evaluatie.value!!)) {
                    evaluatieRepository.setSynched(_evaluatie.value!!)
                }
            } else {
                createWorkerToPersistsWhenOnline()
                sendOfflineMessage()
            }
            progress.hide()
            _persisterenVoltooid.value = true
        }
    }

    /**
     * Function that creates a [NetworkPersistenceWorker] to persist [Evaluatie] when Internet connection is available
     */
    private fun createWorkerToPersistsWhenOnline() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val nwWorker = OneTimeWorkRequestBuilder<NetworkPersistenceWorker>()
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                OneTimeWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.SECONDS
            )
            .build()
    }
    /**
     * This function shows a [Toast] when no Internet connection is available
     */
    private fun sendOfflineMessage() {
        val text = R.string.offline_message
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    /**
     * Function that deletes a temporary [Evaluatie] from Room database.
     * @see EvaluatieRepository
     */
    fun deleteTempEvaluatie() = runBlocking {
        evaluatieRepository.verwijderTempEvaluatie()
    }

    /**
     * Function for changing private LiveData object for communicating with the Fragment indicating that persisting is completed
     */
    fun navigatieNaPersisterenVoltooidCompleted() {
        _persisterenVoltooid.value = false
    }

    /**
     * Function that is called when the [CriteriumOverzichtViewModel] is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}