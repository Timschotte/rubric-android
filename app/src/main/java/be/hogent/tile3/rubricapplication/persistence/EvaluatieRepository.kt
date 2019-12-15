package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import android.util.Log
import android.widget.Toast
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.network.NetworkRubricEvaluatie
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asDatabaseModel
import be.hogent.tile3.rubricapplication.network.asNetworkModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import javax.inject.Inject
/**
 * Repository for [Evaluatie] for Room database operations
 * @constructor Creates a [EvaluatieRepository]
 * @property evaluatieDao DataAccessObject ([EvaluatieDao])for [Evaluatie]
 * @property criteriumEvaluatieDao DataAccessObject ([CriteriumEvaluatieDao]) for Criterium
 * @property rubricApi API for backend communication
 */
class EvaluatieRepository(private val evaluatieDao: EvaluatieDao, private val criteriumEvaluatieDao: CriteriumEvaluatieDao){
    /**
     * Properties
     */
    @Inject
    lateinit var context: Context
    @Inject
    lateinit var rubricApi: RubricApi
    /**
     * Constructor
     */
    init {
        App.component.inject(this)
    }
    /**
     * Co-Routine for inserting an [Evaluatie] in Room database. This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be inserted
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun insert(evaluatie: Evaluatie) : Long  {
        return withContext(Dispatchers.IO){
            evaluatieDao.insert(evaluatie)
        }
    }
    /**
     * Co-Routine for updating an [Evaluatie] in Room database. This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be updated
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun update(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.update(evaluatie)
        }
    }
    /**
     * Co-Routine for retrieving an [Evaluatie] from Room database. This method runs on the IO thread as a background task
     * @param evaluatieId ID from [Evaluatie] to be retrieved
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun get(evaluatieId: String): Evaluatie?{
        return withContext(Dispatchers.IO){
            val evaluatie = evaluatieDao.get(evaluatieId)
            evaluatie
        }
    }
    /**
     * Co-Routine for deleting an [Evaluatie] from Room database. This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be deleted
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun delete(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.delete(evaluatie)
        }
    }
    /**
     * Co-Routine for retrieving an [Evaluatie] for a given Rubric and Student from Room database. This method runs on the IO thread as a background task
     * @param rubricId ID from given Rubric
     * @param studentId ID from given Student
     * @return [Evaluatie]
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun getByRubricAndStudent(rubricId: Long, studentId: Long, temp : Boolean = false): Evaluatie? {
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieDao.getTempByRubricAndStudent(rubricId, studentId)
            if(evaluatie?.evaluatieId == "" || !temp) {
                evaluatie = evaluatieDao.getByRubricAndStudent(rubricId, studentId)
            }
            evaluatie
        }
    }
    /**
     * Co-Routine for deleting temporary [Evaluatie] from Room database. This method runs on the IO thread as a background task
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun verwijderTempEvaluatie() {
        withContext(Dispatchers.IO) {
            evaluatieDao.verwijderTempEvaluatie()
        }
    }
    /**
     * Co-Routine for persisting a temporary [Evaluatie] into a permanent [Evaluatie] in Room database. This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be inserted
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun persisteerTemp(evaluatie: Evaluatie) {
        withContext(Dispatchers.IO){
            val bestaandeEvaluatie = evaluatieDao.getByRubricAndStudent(evaluatie.rubricId, evaluatie.studentId)
            bestaandeEvaluatie?.let {
                criteriumEvaluatieDao.verwijderBestaandeCriteriumEvaluaties(it.evaluatieId)
                evaluatieDao.delete(it)
            }
            evaluatie.evaluatieId = UUID.randomUUID().toString()
            evaluatie.sync = false
            evaluatieDao.insert(evaluatie)
            criteriumEvaluatieDao.koppelTempAanNieuw(evaluatie.evaluatieId)
            evaluatie.criteriumEvaluaties = criteriumEvaluatieDao.getAllForEvaluatie(evaluatie.evaluatieId)
            verwijderTempEvaluatie()
        }
    }
    /**
     * Co-Routine for persisting all [Evaluatie] that are not persisted to the backend API.
     * This method runs on the IO thread as a background task
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     * @see RubricApi
     */
    suspend fun persistEvaluationNotSynchedToNetwork(){
        val notSynched = evaluatieDao.getNotSynched()
        withContext(Dispatchers.IO){
            notSynched.value?.forEach { eval -> persistEvaluationToNetwork(eval) }
        }
    }
    /**
     * Co-Routine for persisting an [Evaluatie] to the backend API. This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be persisted
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
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
                    succes = false
                }
            }
            succes
        }
    }
    /**
     * Co-Routine for updating SyncStatus for an [Evaluatie] in Room database. This method runs on the IO thread as a background task
     * @param evaluatie [Evaluatie] to be inserted
     * @param synced [Boolean]
     * @see evaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun setSynched(evaluatie: Evaluatie, synced : Boolean = true){
        withContext(Dispatchers.IO) {
            evaluatie.sync = synced
            update(evaluatie)
        }
    }

    /**
     * Co-Routine for synchronizing all [Evaluatie] from a given Rubric and Docent on backend API to Room database.
     * This method runs on the IO thread as a background task
     * @param rubricId  ID from given Rubric
     * @param docentId ID from given Docent
     * @see evaluatieDao
     * @see rubricApi
     * @see withContext
     * @see Dispatchers.IO
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
                Log.i("RubricsLogging", "An error occured while fetching evaluations from repository")
            }
        }
    }
}