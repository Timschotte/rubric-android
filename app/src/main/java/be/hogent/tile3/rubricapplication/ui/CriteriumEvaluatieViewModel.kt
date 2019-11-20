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

class CriteriumEvaluatieViewModel(): ViewModel(){

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
        App.component.inject(this)
    }

    fun onGeselecteerdCriteriumChanged(criteriumId: String){
        coroutineScope.launch {
            /* wijzigingen opslaan */
            persisteerVorigeCriteriumEvaluatie()

            /* nieuwe criteriumEvaluatie instellen: ophalen uit database of default */
            _criteriumEvaluatie.value = haalOfMaakCriteriumEvaluatieOp(criteriumId)

            /* criteriumNiveaus instellen*/
            _criteriumNiveaus.value = haalNiveausVoorCriteriumOp(criteriumId)


            println(positieGeselecteerdCriteriumNiveau.value)
        }
    }

    fun setGeselecteerdCriteriumNiveau(){
        _geselecteerdCriteriumNiveau.value = _criteriumNiveaus.value?.singleOrNull {
            it.niveauId == _criteriumEvaluatie.value?.behaaldNiveau
        }

        _positieGeselecteerdCriteriumNiveau.value =
            _criteriumNiveaus.value?.indexOf(_criteriumNiveaus.value?.singleOrNull {
                it.niveauId == _criteriumEvaluatie.value?.behaaldNiveau
            })
    }

    private suspend fun persisteerVorigeCriteriumEvaluatie(){
        var criteriumEvaluatie = _criteriumEvaluatie.value
        withContext(Dispatchers.IO){
            if(criteriumEvaluatie != null)
                criteriumEvaluatieRepository.update(criteriumEvaluatie)
        }
    }

    private suspend fun haalOfMaakCriteriumEvaluatieOp(criteriumId: String): CriteriumEvaluatie{
        return withContext(Dispatchers.IO){
            val criteriumEvaluatie: CriteriumEvaluatie = criteriumEvaluatieRepository
                .getForEvaluatieAndCriterium(TEMP_EVALUATIE_ID, criteriumId)
            criteriumEvaluatie
        }
    }

    private suspend fun haalNiveausVoorCriteriumOp (criteriumId: String): List<Niveau>{
        return withContext(Dispatchers.IO) {
            niveauRepository.getNiveausForCriterium(criteriumId)
        }
    }

    fun onNiveauClicked(niveauId: Long, positie: Int){
        _geselecteerdCriteriumNiveau.value = criteriumNiveaus.value?.singleOrNull{it.niveauId == niveauId}
        _positieGeselecteerdCriteriumNiveau?.value = positie
        _criteriumEvaluatie.value?.behaaldNiveau = niveauId
        _criteriumEvaluatie.value?.score = geselecteerdCriteriumNiveau.value?.ondergrens ?: 0
        Log.i("CriteriumEvaluatieVM","Voor criterium " +
                " is het geselecteerde niveau " + criteriumEvaluatie.value?.behaaldNiveau +
                " met een score van " + criteriumEvaluatie.value?.score +
                " en commentaar \"" + criteriumEvaluatie.value?.commentaar + "\"")

    }

    fun onScoreChanged(oudeScore: Int, nieuweScore: Int){
        Log.i("CriteriumEvaluatieVM", "Numberpicker score veranderd van " + oudeScore + " naar " + nieuweScore)
        _criteriumEvaluatie.value?.score = nieuweScore
        // Todo: persisteren

    }

    fun onCommentaarChanged(oudeCommentaar: String, nieuweCommentaar: String){
        Log.i("CriteriumEvaluatieVM", "Oude commentaar: " + oudeCommentaar + "\nNieuwe commentaar: " + nieuweCommentaar)
        _criteriumEvaluatie.value?.commentaar = nieuweCommentaar
        // Todo: persisteren

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}