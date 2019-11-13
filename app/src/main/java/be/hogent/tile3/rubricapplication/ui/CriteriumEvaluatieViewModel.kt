package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.persistence.CriteriumRepository
import be.hogent.tile3.rubricapplication.persistence.NiveauRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class CriteriumEvaluatieViewModel: ViewModel(){

    @Inject lateinit var niveauRepository: NiveauRepository
    @Inject lateinit var criteriumRepository: CriteriumRepository
    // todo: evaluatierepository maken en injecteren
    // todo: criteriumevaluatierepository maken en injecteren

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _criteriumEvaluatie: LiveData<CriteriumEvaluatie> = getDummyCriteriumEvaluatie()
    val criteriumEvaluatie: LiveData<CriteriumEvaluatie>
        get() = _criteriumEvaluatie

    private val _criteriumNiveaus = MutableLiveData<List<Niveau>>()
    val criteriumNiveaus: LiveData<List<Niveau>>
        get() = _criteriumNiveaus

    private val _geselecteerdCriteriumNiveau = MediatorLiveData<Niveau>()
    val geselecteerdCriteriumNiveau: LiveData<Niveau>
        get() = _geselecteerdCriteriumNiveau

    private val _positieGeselecteerdCriteriumNiveau = MediatorLiveData<Int>()
    val positieGeselecteerdCriteriumNiveau: LiveData<Int>
        get() = _positieGeselecteerdCriteriumNiveau

    init{
        _geselecteerdCriteriumNiveau.addSource(_criteriumNiveaus) { result: List<Niveau> ->
            if (!result.isNullOrEmpty()) {
                _geselecteerdCriteriumNiveau.value = result.singleOrNull {
                    it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
                }
                Log.i("CriteriumEvaluatieVM", "Geselecteerd niveau: " + geselecteerdCriteriumNiveau.value?.omschrijving)
            }
        }
        _positieGeselecteerdCriteriumNiveau.addSource(_criteriumNiveaus){ result: List<Niveau> ->
            if(result != null) {
                _positieGeselecteerdCriteriumNiveau.value =
                    result.indexOf(result.singleOrNull {
                        it.niveauId == criteriumEvaluatie.value?.behaaldNiveau
                    })
                Log.i("CriteriumEvaluatieVM", "Positie geselecteerd niveau: " + positieGeselecteerdCriteriumNiveau.value.toString())
            }
        }

        App.component.inject(this)
    }

    fun onGeselecteerdCriteriumChanged(criteriumId: String){
        coroutineScope.launch {
            _criteriumNiveaus.value = haalNiveausVoorCriteriumOp(criteriumId)
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