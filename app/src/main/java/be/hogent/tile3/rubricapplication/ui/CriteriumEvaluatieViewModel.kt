package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.*
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.persistence.CriteriumEvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.*
import javax.inject.Inject

class CriteriumEvaluatieViewModel: ViewModel(){

    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    @Inject lateinit var criteriumEvaluatieRepository: CriteriumEvaluatieRepository
    // todo: evaluatierepository maken en injecteren
    // todo: criteriumevaluatierepository maken en injecteren

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _criteriumEvaluatie = MutableLiveData<CriteriumEvaluatie>()
    val criteriumEvaluatie: LiveData<CriteriumEvaluatie>
        get() = _criteriumEvaluatie

    private val _criteriumNiveaus = MutableLiveData<List<Niveau>>()
    val criteriumNiveaus: LiveData<List<Niveau>>
        get() = _criteriumNiveaus

    private val _geselecteerdCriteriumNiveau = MutableLiveData<Niveau>()
    val geselecteerdCriteriumNiveau: LiveData<Niveau>
        get() = _geselecteerdCriteriumNiveau

    private val _positieGeselecteerdCriteriumNiveau = MutableLiveData<Int>()
    val positieGeselecteerdCriteriumNiveau: LiveData<Int>
        get() = _positieGeselecteerdCriteriumNiveau

    init{
//        _criteriumEvaluatie.addSource(_criteriumId){ result: String ->
//            if(!result.isNullOrEmpty()){
//                _criteriumEvaluatie.value = criteriumEvaluatieRepository
//                    .getForEvaluatieAndCriterium(TEMP_EVALUATIE_ID, result)
//            }
//        }
//        _geselecteerdCriteriumNiveau.addSource(_criteriumNiveaus) { result: List<Niveau> ->
//            if (!result.isNullOrEmpty()) {
//                _geselecteerdCriteriumNiveau.value = result.singleOrNull {
//                    it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
//                }
//                Log.i("CriteriumEvaluatieVM", "Geselecteerd niveau: " + geselecteerdCriteriumNiveau.value?.omschrijving)
//            }
//        }
//        _positieGeselecteerdCriteriumNiveau.addSource(_criteriumNiveaus){ result: List<Niveau> ->
//            if(result != null) {
//                _positieGeselecteerdCriteriumNiveau.value =
//                    result.indexOf(result.singleOrNull {
//                        it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
//                    })
//                Log.i("CriteriumEvaluatieVM", "Positie geselecteerd niveau: " + positieGeselecteerdCriteriumNiveau.value.toString())
//            }
//        }

        App.component.inject(this)
    }

    fun onGeselecteerdCriteriumChanged(criteriumId: String){
        coroutineScope.launch {
            /* wijzigingen opslaan */
            persisteerVorigeCriteriumEvaluatie()

            /* nieuwe criteriumEvaluatie instellen */
            _criteriumEvaluatie.value = haalCriteriumEvaluatieOp(criteriumId)

            /* criteriumNiveaus instellen*/
            _criteriumNiveaus.value = haalNiveausVoorCriteriumOp(criteriumId)

            _geselecteerdCriteriumNiveau.value = _criteriumNiveaus.value?.singleOrNull {
                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
            }

            _positieGeselecteerdCriteriumNiveau.value =
                _criteriumNiveaus.value?.indexOf(_criteriumNiveaus.value?.singleOrNull {
                    it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
                })
        }
    }

//    fun onGeselecteerdeCriteriumEvaluatieChanged(criteriumEvaluatieId: String){
//        coroutineScope.launch {
//            //TODO: zorgen dat criteriumEvaluatie geinitialiseerd is in overzicht
//            //criteriumEvaluatieRepository.update(_criteriumEvaluatie.value!!)
//
//            _criteriumEvaluatie.value = criteriumEvaluatieRepository.get(criteriumEvaluatieId)
//
//            _geselecteerdCriteriumNiveau.value = _criteriumNiveaus.value?.singleOrNull {
//                it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
//            }
//
//            _positieGeselecteerdCriteriumNiveau.value =
//                _criteriumNiveaus.value?.indexOf(_criteriumNiveaus.value?.singleOrNull {
//                    it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
//                })
//        }
//
//    }

    private suspend fun persisteerVorigeCriteriumEvaluatie(){
        withContext(Dispatchers.IO){
            var criteriumEvaluatie = _criteriumEvaluatie.value
            if(criteriumEvaluatie != null)
                criteriumEvaluatieRepository.update(criteriumEvaluatie)
        }
    }

    private suspend fun haalCriteriumEvaluatieOp(criteriumId: String): CriteriumEvaluatie{
        return withContext(Dispatchers.IO){
            criteriumEvaluatieRepository.getForEvaluatieAndCriterium(TEMP_EVALUATIE_ID, criteriumId)
        }
    }

    private suspend fun haalNiveausVoorCriteriumOp (criteriumId: String): List<Niveau>{
        return withContext(Dispatchers.IO) {
            niveauRepository.getNiveausForCriterium(criteriumId)
        }
    }

    fun onNiveauClicked(niveauId: String, positie: Int){
        _geselecteerdCriteriumNiveau.value = criteriumNiveaus.value?.singleOrNull{it.niveauId == niveauId}
        _positieGeselecteerdCriteriumNiveau?.value = positie
        criteriumEvaluatie.value?.behaaldNiveau = niveauId
        criteriumEvaluatie.value?.score = geselecteerdCriteriumNiveau.value?.ondergrens ?: 0
        Log.i("CriteriumEvaluatieVM","Voor criterium " +
                " is het geselecteerde niveau " + criteriumEvaluatie.value?.behaaldNiveau +
                " met een score van " + criteriumEvaluatie.value?.score +
                " en commentaar \"" + criteriumEvaluatie.value?.commentaar + "\"")
        // Todo: persisteren
    }

    fun onScoreChanged(oudeScore: Int, nieuweScore: Int){
        Log.i("CriteriumEvaluatieVM", "Numberpicker score veranderd van " + oudeScore + " naar " + nieuweScore)
        criteriumEvaluatie.value?.score = nieuweScore
        // Todo: persisteren
    }

    fun onCommentaarChanged(oudeCommentaar: String, nieuweCommentaar: String){
        Log.i("CriteriumEvaluatieVM", "Oude commentaar: " + oudeCommentaar + "\nNieuwe commentaar: " + nieuweCommentaar)
        criteriumEvaluatie.value?.commentaar = nieuweCommentaar
        // Todo: persisteren
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
fun getDummyCriteriumEvaluatie(): MutableLiveData<CriteriumEvaluatie>{
    var result = MutableLiveData<CriteriumEvaluatie>()
    result.value =
        CriteriumEvaluatie("1","1","1","3",null,null)
    return result
}