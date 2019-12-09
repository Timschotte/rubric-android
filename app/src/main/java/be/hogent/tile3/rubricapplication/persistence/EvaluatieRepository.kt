package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.network.NetworkRubricEvaluatie
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asDatabaseModel
import be.hogent.tile3.rubricapplication.network.asNetworkModel
import be.hogent.tile3.rubricapplication.utils.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.*
import javax.inject.Inject

class EvaluatieRepository(private val evaluatieDao: EvaluatieDao, private val criteriumEvaluatieDao: CriteriumEvaluatieDao){

    @Inject
    lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    suspend fun insert(evaluatie: Evaluatie) : Long  {
        return withContext(Dispatchers.IO){
            evaluatieDao.insert(evaluatie)
        }
    }

    suspend fun update(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.update(evaluatie)
        }
    }

    suspend fun get(evaluatieId: String): Evaluatie?{
        return withContext(Dispatchers.IO){
            val evaluatie = evaluatieDao.get(evaluatieId)
            evaluatie
        }
    }

    suspend fun delete(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.delete(evaluatie)
        }
    }

    suspend fun getByRubricAndStudent(rubricId: Long, studentId: Long, temp : Boolean = false): Evaluatie? {
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieDao.getTempByRubricAndStudent(rubricId, studentId)
            if(evaluatie?.evaluatieId == "" || !temp) {
                evaluatie = evaluatieDao.getByRubricAndStudent(rubricId, studentId)
            }
            evaluatie
        }
    }

    suspend fun verwijderTempEvaluatie() {
        withContext(Dispatchers.IO) {
            evaluatieDao.verwijderTempEvaluatie()
        }
    }

    /**
     * Deze functie zal een tijdelijke evaluatie en bijhorende criteriumevaluaties persisteren tot een permanente evaluatie
     */
    suspend fun persisteerTemp(evaluatie: Evaluatie) {
        //Als er één bestaat, moet de GUID overgenomeen worden
        //Als er geen bestaat, moet een nieuwe GUID gekoppeld worden.
        withContext(Dispatchers.IO){
            val bestaandeEvaluatie = evaluatieDao.getByRubricAndStudent(evaluatie.rubricId, evaluatie.studentId)
            bestaandeEvaluatie?.let {
                criteriumEvaluatieDao.verwijderBestaandeCriteriumEvaluaties(it.evaluatieId)
                evaluatieDao.delete(it)
            }
            evaluatie.evaluatieId = UUID.randomUUID().toString()
            evaluatie.sync = false //Set sync false to make sure evaluation is sent
            evaluatieDao.insert(evaluatie)
            criteriumEvaluatieDao.koppelTempAanNieuw(evaluatie.evaluatieId)
            evaluatie.criteriumEvaluaties = criteriumEvaluatieDao.getAllForEvaluatie(evaluatie.evaluatieId)
            verwijderTempEvaluatie()
        }
    }

    suspend fun persistEvaluationNotSynchedToNetwork(){
        val notSynched = evaluatieDao.getNotSynched()
        withContext(Dispatchers.IO){
            notSynched.value?.forEach { eval -> persistEvaluationToNetwork(eval) }
        }
    }

    suspend fun persistEvaluationToNetwork(evaluatie: Evaluatie) : Boolean {

        return withContext(Dispatchers.IO) {
            var succes = true
            evaluatie.let {
                try {
                    val existing = rubricApi.getEvaluaties(mapOf("rubricId" to it.rubricId.toString(),
                        "studentId" to it.studentId.toString(),
                        "docent" to it.docentId.toString())).await().singleOrNull()

                    val networkRubricEvaluatie: NetworkRubricEvaluatie = it.asNetworkModel(existing?.id)

                    if (networkRubricEvaluatie.id != null){
                        rubricApi.updateEvalutatie(networkRubricEvaluatie.id, networkRubricEvaluatie).execute()
                    } else {
                        rubricApi.saveEvaluatie(networkRubricEvaluatie).execute()
                    }
                    setSynched(it, true)
                } catch (e: Exception) {
                    Log.i("TestN", "API - Try Exception: " + e.message)
                    succes = false
                }
            }
            succes
        }
    }

    suspend fun setSynched(evaluatie: Evaluatie, synched : Boolean = true){
        withContext(Dispatchers.IO) {
            evaluatie.sync = synched
            update(evaluatie)
        }
    }

    /**
     * Connectie met netwerk voor backend  evaluaties
     */
    suspend fun refreshEvaluations(rubricId: Long, docentId: Long){
        withContext(Dispatchers.IO) {
            try {
                val evaluaties = rubricApi.getEvaluaties(
                    mapOf("rubricId" to rubricId.toString(), "docentId" to docentId.toString()))
                    .await()
                evaluaties.forEach { eval ->
                    if(!evaluatieDao.tempEvaluationExists(eval.rubricId, eval.studentId).blockingGet()){
                        val evalDb = eval.asDatabaseModel()
                        //Check of lokale versie al gesynched is. Indien niet, lokale versie behouden en server versie negeren.
                        val existingEval = evaluatieDao.getByRubricAndStudent(eval.rubricId, eval.studentId)
                        var insert = true

                        existingEval?.let{ ex ->
                            if(ex.sync){
                                evalDb.evaluatieId = ex.evaluatieId
                                evalDb.criteriumEvaluaties.forEach { ce -> ce.evaluatieId = ex.evaluatieId }
                            }else {
                                insert = false
                            }
                        }
                        if(insert){
                            evaluatieDao.insert(evalDb)
                            criteriumEvaluatieDao.insertAll(evalDb.criteriumEvaluaties)
                        }
                    }
                }

            } catch (e: IOException) {
                Log.i("RubricRepository", e.message)
            }
        }
    }
}