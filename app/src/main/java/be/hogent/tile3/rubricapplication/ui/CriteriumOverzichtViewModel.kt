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
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import kotlinx.coroutines.*
import javax.inject.Inject


class CriteriumOverzichtViewModel: ViewModel(){

    @Inject lateinit var context: Context

    @Inject lateinit var rubricRepository: RubricRepository
    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    // todo: evaluatierepository maken en injecteren
    // todo: criteriumevaluatierepository maken en injecteren


    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _huidigeEvaluatie = getDummyEvaluatie()
    val huidigeEvaluatie: LiveData<Evaluatie>
        get() = _huidigeEvaluatie

    private var _huidigeCriteriumEvaluaties = getDummyCriteriumEvaluaties()

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
        // TODO: EvaluatieId doorkrijgen, Evaluatie op basis daarvan ophalen------------------------
        var evaluatie = Evaluatie("1","1","1")
        //------------------------------------------------------------------------------------------
        coroutineScope.launch{
            _huidigeRubric.addSource(
                rubricRepository.get(evaluatie.rubricId),
                _huidigeRubric::setValue
            )
            _rubricCriteria.addSource(
                criteriumRepository.getCriteriaForRubric(evaluatie.rubricId)
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

    private fun refreshRubricDatabase() {
        if (isNetworkAvailable()){
            coroutineScope.launch {
                rubricRepository.refreshRubrics()
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
/*
    private fun getRubrics(){
        coroutineScope.launch {
            var rubricsDeferred = rubricRepository.rubricApi.getRubrics()
            try {
                val result = rubricsDeferred.await()
                _rubrics.value = result
                Log.i("Test", "Done")
            } catch (e: Exception){
                Log.i("Test", e.message)
                _rubrics.value = ArrayList()
            }
        }
    }*/

    fun onCriteriumClicked(criteriumId: String, positie: Int){
        _geselecteerdCriterium.value = rubricCriteria.value?.singleOrNull{it?.criteriumId == criteriumId}
        _positieGeselecteerdCriterium?.value = positie

        Log.i("CriteriumOverzichtVM","Criterium " + geselecteerdCriterium.value?.naam +
                " op positie " + positieGeselecteerdCriterium.value.toString() +
                " werd geselecteerd.")
        // Todo: persisteren
    }

    fun onUpEdgeButtonClicked(){
        Log.i("CriteriumEvaluatieVM","Up Edge Button Clicked")
        onEdgeButtonClicked(Direction.UP)
    }

    fun onDownEdgeButtonClicked(){
        Log.i("CriteriumEvaluatieVM","Down Edge Button Clicked")
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

fun getDummyEvaluatie(): MutableLiveData<Evaluatie>{
    var result = MutableLiveData<Evaluatie>()
    result.value = Evaluatie("1", "1", "1")
    return result
}

fun getDummyCriteriumEvaluaties(): MutableLiveData<List<CriteriumEvaluatie>>{
    var result = MutableLiveData<List<CriteriumEvaluatie>>()
    result.value = listOf(
        CriteriumEvaluatie("1","1","1","3",null,null),
        CriteriumEvaluatie("1","1","2","7",null,null),
        CriteriumEvaluatie("1","1","3","9",null,null)
    )
    return result
}