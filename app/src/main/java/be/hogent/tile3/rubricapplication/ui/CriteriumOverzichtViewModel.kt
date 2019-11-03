package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import javax.inject.Inject


class CriteriumOverzichtViewModel: ViewModel(){
    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    // todo: evaluatierepository maken en injecteren
    // todo: criteriumevaluatierepository maken en injecteren

    //val huidigeEvaluatie: MediatorLiveData<Evaluatie>

    private val _rubricCriteria: MediatorLiveData<List<Criterium>> = getDummyCriteria()
    val rubricCriteria: LiveData<List<Criterium>>
        get() = _rubricCriteria

    private val _geselecteerdCriterium: MutableLiveData<Criterium> = getInitieelGeselecteerdCriterium()
    val geselecteerdCriterium: LiveData<Criterium>
        get() = _geselecteerdCriterium

    private val _positieGeselecteerdCriterium: MutableLiveData<Int> = getInitielePositie()
    val positieGeselecteerdCriterium: LiveData<Int>
        get() = _positieGeselecteerdCriterium

    init{
        _geselecteerdCriterium.value = rubricCriteria.value?.get(0)
        App.component.inject(this)
    }

    fun getInitielePositie(): MutableLiveData<Int>{
        var result = MutableLiveData<Int>()
        result.value = 0
        return result
    }

    fun getInitieelGeselecteerdCriterium(): MutableLiveData<Criterium>{
        var result = MutableLiveData<Criterium>()
        result.value = Criterium("", "", "", 0.0, "")
        return result
    }

    fun onCriteriumClicked(criteriumId: String, positie: Int){
        _geselecteerdCriterium.value = rubricCriteria.value?.singleOrNull{it.criteriumId == criteriumId}
        _positieGeselecteerdCriterium?.value = positie

        Log.i("CriteriumOverzichtVM","Criterium " + geselecteerdCriterium.value?.naam +
                " op positie " + positieGeselecteerdCriterium.value.toString() +
                " werd geselecteerd.")
        // Todo: persisteren
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
        Criterium("c1", LOREM_4, LOREM_8, 0.1, "r1"),
        Criterium("c2", LOREM_5, LOREM_9, 0.2, "r1"),
        Criterium("c3", LOREM_6, LOREM_10, 0.1, "r1"),
        Criterium("c4", LOREM_7, LOREM_11, 0.05, "r1"),
        Criterium("c5", LOREM_7, LOREM_8, 0.1, "r1"),
        Criterium("c6", LOREM_6, LOREM_9, 0.2, "r1"),
        Criterium("c7", LOREM_5, LOREM_10, 0.1, "r1"),
        Criterium("c8", LOREM_4, LOREM_11, 0.05, "r1")
    )
    return result
}
fun getDummyCriteriumNiveaus(): MutableLiveData<List<Niveau>>{
    var result = MutableLiveData<List<Niveau>>()
    result.value = listOf(
        Niveau("cn11","Ontoereikend", LOREM_3, 0, 6, -2, "c1"),
        Niveau("cn12","Beginnend", LOREM_2, 7, 9, -1, "c1"),
        Niveau("cn13","Lerend", LOREM_1, 10, 11, 0, "c1"),
        Niveau("cn14","Gevorderd", LOREM_2, 12, 15, 1, "c1"),
        Niveau("cn15","Excellerend", LOREM_1, 16, 20, 2, "c1"),

        Niveau("cn21","Ontoereikend", LOREM_3, 0, 6, -1, "c2"),
        Niveau("cn23","Lerend", LOREM_1, 10, 11, 0, "c2"),
        Niveau("cn24","Gevorderd", LOREM_2, 12, 15, 1, "c2"),
        Niveau("cn25","Excellerend", LOREM_1, 16, 20, 2, "c2"),

        Niveau("cn31","Ontoereikend", LOREM_2, 0, 6, -2, "c3"),
        Niveau("cn32","Beginnend", LOREM_2, 7, 9, -1, "c3"),
        Niveau("cn33","Lerend", LOREM_1, 10, 11, 0, "c3"),
        Niveau("cn34","Gevorderd", LOREM_3, 12, 15, 1, "c3"),
        Niveau("cn35","Excellerend", LOREM_1, 16, 20, 2, "c3"),

        Niveau("cn41","Ontoereikend", LOREM_2, 0, 6, -1, "c4"),
        Niveau("cn43","Lerend", LOREM_1, 10, 11, 0, "c4"),
        Niveau("cn44","Gevorderd", LOREM_3, 12, 15, 1, "c4"),

        Niveau("cn51","Ontoereikend", LOREM_3, 0, 6, -2, "c5"),
        Niveau("cn52","Beginnend", LOREM_2, 7, 9, -1, "c5"),
        Niveau("cn53","Lerend", LOREM_1, 10, 11, 0, "c5"),
        Niveau("cn54","Gevorderd", LOREM_2, 12, 15, 1, "c5"),
        Niveau("cn55","Excellerend", LOREM_1, 16, 20, 2, "c5"),

        Niveau("cn61","Ontoereikend", LOREM_3, 0, 6, -1, "c6"),
        Niveau("cn63","Lerend", LOREM_1, 10, 11, 0, "c6"),
        Niveau("cn64","Gevorderd", LOREM_2, 12, 15, 1, "c6"),
        Niveau("cn65","Excellerend", LOREM_1, 16, 20, 2, "c6"),

        Niveau("cn71","Ontoereikend", LOREM_2, 0, 6, -2, "c7"),
        Niveau("cn72","Beginnend", LOREM_2, 7, 9, -1, "c7"),
        Niveau("cn73","Lerend", LOREM_1, 10, 11, 0, "c7"),
        Niveau("cn74","Gevorderd", LOREM_3, 12, 15, 1, "c7"),
        Niveau("cn75","Excellerend", LOREM_1, 16, 20, 2, "c7"),

        Niveau("cn81","Ontoereikend", LOREM_2, 0, 6, -1, "c8"),
        Niveau("cn83","Lerend", LOREM_1, 10, 11, 0, "c8"),
        Niveau("cn84","Gevorderd", LOREM_3, 12, 15, 1, "c8")
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
const val LOREM_10 = "Quisque lacinia justo non lacus convallis, id lobortis velit mollis.\n" +
        "Vestibulum ut arcu egestas nisl convallis pretium quis in dolor.\n" +
        "Donec nibh purus, dictum ac sollicitudin in, euismod vel libero."
const val LOREM_11 = "Phasellus non auctor urna. Integer efficitur nibh in consequat rhoncus. " +
        "Donec laoreet mi nec justo pellentesque commodo. Pellentesque molestie neque nec nisi " +
        "commodo, gravida mattis magna aliquam."
