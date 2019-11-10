package be.hogent.tile3.rubricapplication.ui

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.network.NetworkRubric
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


class CriteriumOverzichtViewModel: ViewModel(){

    @Inject lateinit var rubricRepository: RubricRepository
    @Inject lateinit var context: Context

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var rubrics: LiveData<List<Rubric>>
    lateinit var criteria: LiveData<List<Criterium>>

    //this line makes app crash..
    /*val rubrics = rubricRepository.rubrics*/

    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    // todo: evaluatierepository maken en injecteren
    // todo: criteriumevaluatierepository maken en injecteren

    //val huidigeEvaluatie: MediatorLiveData<Evaluatie>

    /*private val _rubrics = MutableLiveData<List<Rubric>>()
    val rubrics: LiveData<List<Rubric>>
        get() = _rubrics*/


    private val _rubricCriteria: MediatorLiveData<List<Criterium>> = getDummyCriteria()
    val rubricCriteria: LiveData<List<Criterium>>
        get() = _rubricCriteria

    private val _geselecteerdCriterium = MutableLiveData<Criterium>()
    val geselecteerdCriterium: LiveData<Criterium>
        get() = _geselecteerdCriterium

    private val _positieGeselecteerdCriterium = MutableLiveData<Int>()
    val positieGeselecteerdCriterium: LiveData<Int>
        get() = _positieGeselecteerdCriterium

    // Variabele wordt gebruikt om performantieredenen; zo moet de grootte van de lijst criteria
    // slechts 1x berekend worden tijdens een evaluatie.
    private val _positieLaatsteCriterium = MutableLiveData<Int>()
    val positieLaatsteCriterium: LiveData<Int>
        get() = _positieLaatsteCriterium

    private val _overzichtPaneelUitgeklapt = MutableLiveData<Boolean>().apply{ postValue(true)}
    val overzichtPaneelUitgeklapt: LiveData<Boolean>
        get() = _overzichtPaneelUitgeklapt

    init{
        App.component.inject(this)
        rubrics = rubricRepository.rubrics
        criteria = rubricRepository.criteria
        Log.i("CriteriumOverzichtVM", "Init-block starts execution")
        _geselecteerdCriterium.value = rubricCriteria.value?.get(0)
        _positieGeselecteerdCriterium.value = 0
        var grootteRubricCriteria: Int? = rubricCriteria.value?.size
        _positieLaatsteCriterium.value = if(grootteRubricCriteria == null) 0 else (grootteRubricCriteria -1)

        /*getRubrics()*/

        refreshRubricDatabase()
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
        _geselecteerdCriterium.value = rubricCriteria.value?.singleOrNull{it.criteriumId == criteriumId}
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

fun getDummyRubric(): MediatorLiveData<Rubric>{
    var result = MediatorLiveData<Rubric>()
    result.value = Rubric(
        "r1",
        "Dummy Rubric",
        LOREM_11,
        "",
        "")
    return result
}
fun getDummyEvaluatie(): MutableLiveData<Evaluatie>{
    var result = MutableLiveData<Evaluatie>()
    result.value = Evaluatie("e1", "s1", "r1")
    return result
}
fun getDummyCriteria(): MediatorLiveData<List<Criterium>> {
    var result = MediatorLiveData<List<Criterium>>()

    result.value = listOf(
        Criterium("1", LOREM_4, LOREM_8, 0.1, "r1"),
        Criterium("2", LOREM_5, LOREM_9, 0.2, "r1"),
        Criterium("3", LOREM_6, LOREM_10, 0.1, "r1"),
        Criterium("4", LOREM_7, LOREM_11, 0.05, "r1"),
        Criterium("5", LOREM_7, LOREM_8, 0.1, "r1"),
        Criterium("6", LOREM_6, LOREM_9, 0.2, "r1"),
        Criterium("7", LOREM_5, LOREM_10, 0.1, "r1"),
        Criterium("8", LOREM_4, LOREM_11, 0.05, "r1")
    )
    return result
}
fun getDummyCriteriumNiveaus(): MutableLiveData<List<Niveau>>{
    var result = MutableLiveData<List<Niveau>>()
    result.value = listOf(
        Niveau("cn11","Ontoereikend", LOREM_3, 0, 6, -2, "1"),
        Niveau("cn12","Beginnend", LOREM_2, 7, 9, -1, "1"),
        Niveau("cn13","Lerend", LOREM_1, 10, 11, 0, "1"),
        Niveau("cn14","Gevorderd", LOREM_2, 12, 15, 1, "1"),
        Niveau("cn15","Excellerend", LOREM_1, 16, 20, 2, "1"),

        Niveau("cn21","Ontoereikend", LOREM_3, 0, 6, -1, "2"),
        Niveau("cn23","Lerend", LOREM_1, 10, 11, 0, "2"),
        Niveau("cn24","Gevorderd", LOREM_2, 12, 15, 1, "2"),
        Niveau("cn25","Excellerend", LOREM_1, 16, 20, 2, "2"),

        Niveau("cn31","Ontoereikend", LOREM_2, 0, 6, -2, "3"),
        Niveau("cn32","Beginnend", LOREM_2, 7, 9, -1, "3"),
        Niveau("cn33","Lerend", LOREM_1, 10, 11, 0, "3"),
        Niveau("cn34","Gevorderd", LOREM_3, 12, 15, 1, "3"),
        Niveau("cn35","Excellerend", LOREM_1, 16, 20, 2, "3"),

        Niveau("cn41","Ontoereikend", LOREM_2, 0, 6, -1, "4"),
        Niveau("cn43","Lerend", LOREM_1, 10, 11, 0, "4"),
        Niveau("cn44","Gevorderd", LOREM_3, 12, 15, 1, "4"),

        Niveau("cn51","Ontoereikend", LOREM_3, 0, 6, -2, "5"),
        Niveau("cn52","Beginnend", LOREM_2, 7, 9, -1, "5"),
        Niveau("cn53","Lerend", LOREM_1, 10, 11, 0, "5"),
        Niveau("cn54","Gevorderd", LOREM_2, 12, 15, 1, "5"),
        Niveau("cn55","Excellerend", LOREM_1, 16, 20, 2, "5"),

        Niveau("cn61","Ontoereikend", LOREM_3, 0, 6, -1, "6"),
        Niveau("cn63","Lerend", LOREM_1, 10, 11, 0, "6"),
        Niveau("cn64","Gevorderd", LOREM_2, 12, 15, 1, "6"),
        Niveau("cn65","Excellerend", LOREM_1, 16, 20, 2, "6"),

        Niveau("cn71","Ontoereikend", LOREM_2, 0, 6, -2, "7"),
        Niveau("cn72","Beginnend", LOREM_2, 7, 9, -1, "7"),
        Niveau("cn73","Lerend", LOREM_1, 10, 11, 0, "7"),
        Niveau("cn74","Gevorderd", LOREM_3, 12, 15, 1, "7"),
        Niveau("cn75","Excellerend", LOREM_1, 16, 20, 2, "7"),

        Niveau("cn81","Ontoereikend", LOREM_2, 0, 6, -1, "8"),
        Niveau("cn83","Lerend", LOREM_1, 10, 11, 0, "8"),
        Niveau("cn84","Gevorderd", LOREM_3, 12, 15, 1, "8")
    )
    return result
}

const val LOREM_4 = "Lorem Ipsum"
const val LOREM_5 = "Dolor Sit Amet"
const val LOREM_6 = "Consectetur Adipiscing elit Donec eget Tellus"
const val LOREM_7 = "A Tellus Volutpat Pretium"

const val LOREM_8 = "Nullam placerat magna quam, a malesuada metus facilisis at. Aenean eu " +
        "hendrerit metus. Cras nec urna sed nibh cursus fringilla sit amet consectetur sem. " +
        "Aenean eu turpis vel ipsum lacinia scelerisque sit amet eu leo."
const val LOREM_9 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget tellus a tellus volutpat pretium ut nec purus."
const val LOREM_10 = "Quisque lacinia justo non lacus convallis, rubricId lobortis velit mollis.\n" +
        "Vestibulum ut arcu egestas nisl convallis pretium quis in dolor.\n" +
        "Donec nibh purus, dictum ac sollicitudin in, euismod vel libero."
const val LOREM_11 = "Phasellus non auctor urna. Integer efficitur nibh in consequat rhoncus. " +
        "Donec laoreet mi nec justo pellentesque commodo. Pellentesque molestie neque nec nisi " +
        "commodo, gravida mattis magna aliquam."
