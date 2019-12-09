package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.*
import androidx.work.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.R
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


class CriteriumOverzichtViewModel(
    private val rubricId: Long,
    val student: Student
) :
    ViewModel() {

    /* PRIVATE VARIABLES -------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/
    // Context
    @Inject
    lateinit var context: Context

    // Repositories
    @Inject
    lateinit var rubricRepository: RubricRepository
    @Inject
    lateinit var evaluatieRepository: EvaluatieRepository
    @Inject
    lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository


    // Coroutine variables
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val ioScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    /* BACKED PROPERTIES -------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    // DOMAIN
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


    /* INITIALIZATION ----------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/
    init {
        App.component.inject(this)
        _score.value = 0
        _totaalScore.value = 0
    }

    private suspend fun getEvaluatieRubric(rubricId: Long): EvaluatieRubric {
        return withContext(Dispatchers.IO) {
            rubricRepository.getEvaluatieRubric(rubricId)
        }
    }

    private fun initialiseerProperties(data: EvaluatieRubric){
        _geselecteerdCriterium.value = data.criteria[0]
        _positieGeselecteerdCriterium.value = 0
        val grootteRubricCriteria: Int? = data.criteria.size
        _positieLaatsteCriterium.value =
            if (grootteRubricCriteria == null) 0 else (grootteRubricCriteria - 1)
    }

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

    private fun geefEvaluatie(temp: Boolean = false): Evaluatie? = runBlocking{
//        return withContext(Dispatchers.IO) {
        return@runBlocking evaluatieRepository.getByRubricAndStudent(rubricId, student.studentId, temp)
//        }
    }

    private suspend fun geefCriteriumEvaluaties(evaluatieId: String?): List<CriteriumEvaluatie> {
        return withContext(Dispatchers.IO) {
            if (!evaluatieId.isNullOrEmpty())
                criteriumEvaluatieRepository.getAllForEvaluatie(evaluatieId)
            else
                emptyList()
        }
    }

    private suspend fun slaTempEvaluatieOp(obj: Evaluatie) {
        _evaluatie.value = obj
        withContext(Dispatchers.IO) {
            evaluatieRepository.insert(obj)
        }
    }

    private suspend fun slaTempCriteriumEvaluatiesOp(obj: List<CriteriumEvaluatie>) {
        _criteriumEvaluaties.value = obj
        withContext(Dispatchers.IO) {
            criteriumEvaluatieRepository.insertAll(obj)
        }
    }

    /* CLICK HANDLERS ----------------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    fun onGeselecteerdCriteriumGewijzigd(
        criteriumId: Long,
        positie: Int,
        data: EvaluatieRubric? = null
    ) {
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

    fun onScoreChanged(nieuweScore: Int){
        _score.value = _score.value!!.plus(nieuweScore).minus(_criteriumEvaluatie.value?.score!!)
        _criteriumEvaluatie.value?.score = nieuweScore
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
    }

    fun onCommentaarChanged(nieuweCommentaar: String) {
        _criteriumEvaluatie.value?.commentaar = nieuweCommentaar
        persisteerCriteriumEvaluatie(criteriumEvaluatie.value)
        _criteriumEvaluatie.value = _criteriumEvaluatie.value
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
        onGeselecteerdCriteriumGewijzigd(
            geselecteerdCriterium.value?.criteriumId ?: 0,
            nieuwePositie
        )
    }

    fun onKlapInKlapUitButtonClicked() {
        _overzichtPaneelUitgeklapt.value?.let {
            _overzichtPaneelUitgeklapt.value = !it
        }
    }


    /* PERSISTENTIE, BREAKDOWN -------------------------------------------------------------------*/
    /*--------------------------------------------------------------------------------------------*/

    private fun persisteerCriteriumEvaluatie(obj: CriteriumEvaluatie?) {
        ioScope.launch {
            if (obj != null) {
                criteriumEvaluatieRepository.update(obj)
            }
        }
    }

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

    private fun createWorkerToPersistsWhenOnline() {
        //Create worker and set to execute when connection is restored.
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

    private fun sendOfflineMessage() {
        val text = R.string.offline_message
        val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun deleteTempEvaluatie() = runBlocking {
        evaluatieRepository.verwijderTempEvaluatie()
        Log.i("Test blocking", "OK")
    }

    fun navigatieNaPersisterenVoltooidCompleted() {
        _persisterenVoltooid.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}