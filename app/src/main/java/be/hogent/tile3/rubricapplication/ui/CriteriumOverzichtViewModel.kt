package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.*
import be.hogent.tile3.rubricapplication.persistence.*
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.*
import javax.inject.Inject


class CriteriumOverzichtViewModel(private val rubricId: String, private val studentId: Long): ViewModel(){

    @Inject lateinit var context: Context
    @Inject lateinit var rubricRepository: RubricRepository
    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    @Inject lateinit var evaluatieRepository: EvaluatieRepository
    @Inject lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private var _huidigeRubric = MediatorLiveData<Rubric>()

    private val _rubricCriteria = MediatorLiveData<List<Criterium>?>()
    val rubricCriteria: LiveData<List<Criterium>?>
        get() = _rubricCriteria

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

    private val _overzichtPaneelUitgeklapt = MutableLiveData<Boolean>().apply{ postValue(true)}
    val overzichtPaneelUitgeklapt: LiveData<Boolean>
        get() = _overzichtPaneelUitgeklapt

    init{
        Log.i("CriteriumOverzichtVM", "Init-block starts execution")
        // TODO: EvaluatieId doorkrijgen, Evaluatie en EvaluatieCriteria op basis daarvan ophalen
        // TODO: Vervolgens geselecteerdEvaluatieCriterium instellen
        var rubricId = "1"
        //------------------------------------------------------------------------------------------
        coroutineScope.launch{
            prepareData()
            _huidigeRubric.addSource(
                rubricRepository.get(rubricId),
                _huidigeRubric::setValue
            )
            _rubricCriteria.addSource(
                criteriumRepository.getCriteriaForRubric(rubricId)
            ){
                result: List<Criterium>? ->
                if(!result.isNullOrEmpty()){
                    _rubricCriteria.value = result
                }
            }
            _geselecteerdCriterium.addSource(_rubricCriteria){ result: List<Criterium>? ->
                result?.let{
                    _geselecteerdCriterium.value = result[0]
                    _positieGeselecteerdCriterium.value = 0
                    var grootteRubricCriteria: Int? = result.size
                    _positieLaatsteCriterium.value =
                        if(grootteRubricCriteria == null) 0 else (grootteRubricCriteria -1)
                }
            }
        }

        App.component.inject(this)
    }

    private fun prepareData() {
        if (isNetworkAvailable()){
            coroutineScope.launch {
                rubricRepository.refreshRubrics()
                initialiseerDummyEvaluatie()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun onCriteriumClicked(criteriumId: String, positie: Int){
        _geselecteerdCriterium.value = rubricCriteria.value?.singleOrNull{it?.criteriumId == criteriumId}
        _positieGeselecteerdCriterium?.value = positie
    }

    fun onUpEdgeButtonClicked(){
        onEdgeButtonClicked(Direction.UP)
    }

    fun onDownEdgeButtonClicked(){
        onEdgeButtonClicked(Direction.DOWN)
    }

    private enum class Direction{
        UP,
        DOWN
    }

    private fun onEdgeButtonClicked(direction: Direction){
        var oudePositie: Int? = positieGeselecteerdCriterium.value
        var nieuwePositie: Int = if(oudePositie == null) 0 else {
            if(direction == Direction.UP) oudePositie -1 else oudePositie +1
        }

        _geselecteerdCriterium.value = rubricCriteria.value?.get(nieuwePositie)

        _positieGeselecteerdCriterium?.value = nieuwePositie
    }

    fun onKlapInKlapUitButtonClicked(){
        _overzichtPaneelUitgeklapt.value?.let {
            _overzichtPaneelUitgeklapt.value = !it
        }
    }

    fun persisteerEvaluatie(){
        coroutineScope.launch {
            var evaluatie: Evaluatie =
                haalTijdelijkeEvaluatieOp()
            var criteriumEvaluaties: List<CriteriumEvaluatie> =
                haalTijdelijkeCriteriumEvaluatiesOp()
            var evaluatieId = evaluatieRepository.insert(Evaluatie(
                        evaluatie.evaluatieId,
                        /* evaluatie.studentId, */
                        evaluatie.rubricId)
                )
            criteriumEvaluaties.forEach {
                criteriumEvaluatieRepository.insert(CriteriumEvaluatie(
                        it.criteriumEvaluatieId,
                        evaluatieId,
                        it.criteriumId,
                        it.behaaldNiveau,
                        it.score,
                        it.commentaar
                    )
                )
            }
            verwijderTijdelijkeEvaluatieData(evaluatie, criteriumEvaluaties)
        }
    }

    private suspend fun haalTijdelijkeEvaluatieOp(): Evaluatie{
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieRepository.get(TEMP_EVALUATIE_ID)
            evaluatie
        }
    }

    private suspend fun haalTijdelijkeCriteriumEvaluatiesOp(): List<CriteriumEvaluatie>{
        return withContext(Dispatchers.IO){
            var criteriumEvaluaties = criteriumEvaluatieRepository.getAllForEvaluatie(
                TEMP_EVALUATIE_ID)
            criteriumEvaluaties
        }
    }

    private suspend fun verwijderTijdelijkeEvaluatieData(
            evaluatie: Evaluatie,
            criteriumEvaluaties: List<CriteriumEvaluatie>
    ){
        withContext(Dispatchers.IO){
            criteriumEvaluaties.forEach{
                criteriumEvaluatieRepository.delete(it)
            }
            evaluatieRepository.delete(evaluatie)
        }
    }

    private suspend fun initialiseerDummyEvaluatie(){
        withContext(Dispatchers.IO){
            evaluatieRepository.insert(Evaluatie(TEMP_EVALUATIE_ID, /* "1" , */"1"))
            criteriumEvaluatieRepository.insertAll(
                listOf(
                    CriteriumEvaluatie(0L, TEMP_EVALUATIE_ID,"1","3",null,"LoremIpsumTesterdieTest"),
                    CriteriumEvaluatie(0L, TEMP_EVALUATIE_ID,"2","7",null,"HiHiHiHaHaHa"),
                    CriteriumEvaluatie(0L, TEMP_EVALUATIE_ID,"3","9",null,"SleepDeprivationIsADrug")
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}