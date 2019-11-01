package be.hogent.tile3.rubricapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.model.Niveau

class CriteriumEvaluatieViewModel: ViewModel(){
    // CriteriumEvaluatieViewModel kent volgende:
    //   1. De Evaluatie die aan de gang is. Deze Evaluatie kent ook de Rubric en de Student.
    //   2. Het Criterium wat hier geëvalueerd wordt.
    //   3. De (Criterium)Niveaus adhv dewelke de evaluatie moet gebeuren
    //   4. Het CriteriumEvaluatie-object zodat we de evaluatie van de Student voor dit Criterium
    //         kunnen ingeven
    // Voorlopig zet ik hier dummy data, te vervangen met LiveData uit de Repositories

    val huidigCriterium: LiveData<Criterium> = getDummyCriterium()
    val criteriumNiveaus: LiveData<List<Niveau>> = getDummyCriteriumNiveaus()
    val evaluatie: LiveData<Evaluatie> = getDummyEvaluatie()
    val criteriumEvaluatie: LiveData<CriteriumEvaluatie> = getDummyCriteriumEvaluatie()

}
fun getDummyCriterium(): MutableLiveData<Criterium>{
    var result = MutableLiveData<Criterium>()
    result.value = Criterium("c1","Android Programmeren", "Evaluatie van de Android programming skillz van de student. Ongetwijfeld zijn die super-awesome shizzle.", 0.2, "r1")
    return result
}
fun getDummyCriteriumNiveaus(): MutableLiveData<List<Niveau>>{
    var result = MutableLiveData<List<Niveau>>()
    result.value = listOf(
        Niveau("cn11","Ontoereikend", LOREM_3, 0, 6, -2, "c1"),
        Niveau("cn12","Beginnend", LOREM_2, 7, 9, -1, "c1"),
        Niveau("cn13","Lerend", LOREM_1, 10, 11, 0, "c1"),
        Niveau("cn14","Gevorderd", LOREM_2, 12, 15, 1, "c1"),
        Niveau("cn15","Excellerend", LOREM_1, 16, 20, 2, "c1")
    )
    return result
}
fun getDummyEvaluatie(): MutableLiveData<Evaluatie>{
    var result = MutableLiveData<Evaluatie>()
    result.value = Evaluatie("e1", "s1", "r1")
    return result
}
fun getDummyCriteriumEvaluatie(): MutableLiveData<CriteriumEvaluatie>{
    var result = MutableLiveData<CriteriumEvaluatie>()
    result.value = CriteriumEvaluatie("ce1","e1","c1","cn13",null,null)
    return result
}

const val LOREM_1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec eget tellus a tellus volutpat pretium ut nec purus. Nullam placerat magna quam, a malesuada metus facilisis at. Aenean eu hendrerit metus. Cras nec urna sed nibh cursus fringilla sit amet consectetur sem. Aenean eu turpis vel ipsum lacinia scelerisque sit amet eu leo. Pellentesque bibendum velit vitae lorem convallis, quis sagittis mauris imperdiet. Donec convallis commodo tincidunt. In ipsum elit, luctus a accumsan quis, eleifend ultricies ex. Nulla non auctor mauris, eget sodales velit."
const val LOREM_2 = "Quisque lacinia justo non lacus convallis, id lobortis velit mollis.\nVestibulum ut arcu egestas nisl convallis pretium quis in dolor.\nDonec nibh purus, dictum ac sollicitudin in, euismod vel libero.\nPhasellus non auctor urna.\nInteger efficitur nibh in consequat rhoncus.\nDonec laoreet mi nec justo pellentesque commodo.\nPellentesque molestie neque nec nisi commodo, gravida mattis magna aliquam. Curabitur in aliquet dolor. Sed quis erat ligula. Ut vitae orci elit.\nSed tincidunt magna lorem, a pulvinar quam volutpat sit amet."
const val LOREM_3 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit."