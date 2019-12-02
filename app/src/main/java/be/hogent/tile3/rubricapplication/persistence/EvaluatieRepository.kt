package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asDatabaseModel
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*
import javax.inject.Inject

class EvaluatieRepository(private val evaluatieDao: EvaluatieDao, private val criteriumEvaluatieDao: CriteriumEvaluatieDao){

    @Inject
    lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    suspend fun insert(evaluatie: Evaluatie){
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
            val evaluatie = if(temp) evaluatieDao.getTempByRubricAndStudent(rubricId, studentId)
                else evaluatieDao.getByRubricAndStudent(rubricId, studentId)
            evaluatie
        }
    }



    suspend fun verwijderVorigeTempEvaluatie() {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.verwijderTempCriteriumEvaluaties()
            evaluatieDao.verwijderTempEvaluatie()
        }
    }

    /**
     * Deze functie zal een tijdelijke evaluatie en bijhorende criteriumevaluaties persisteren tot een permanente evaluatie
     */
    suspend fun persisteerTemp(evaluatie: Evaluatie) {
        //Als er één bestaat, moet de GUID overgenomeen worden
        //Als er geen bestaat, moet een nieuwe GUID
        withContext(Dispatchers.IO){
            val bestaandeEvaluatie = evaluatieDao.getByRubricAndStudent(evaluatie.rubricId, evaluatie.studentId)
            bestaandeEvaluatie?.let {
                criteriumEvaluatieDao.verwijderBestaandeCriteriumEvaluaties(it.evaluatieId)
                evaluatieDao.delete(it)
            }
            evaluatie.evaluatieId = UUID.randomUUID().toString()
            evaluatieDao.insert(evaluatie)
            criteriumEvaluatieDao.koppelTempAanNieuw(evaluatie.evaluatieId)
            verwijderVorigeTempEvaluatie()

        }
    }

    /**
     * Connectie met netwerk voor backend persistentie evaluaties
     */
    suspend fun refreshEvaluations(rubricId: Int, docentId: Int){
        withContext(Dispatchers.IO) {
            try {
                val evaluaties = rubricApi.getEvaluaties(
                    mapOf("rubricId" to rubricId.toString(), "docentId" to docentId.toString()))
                    .await()
                //evaluatieDao.insert()

            } catch (e: IOException) {
                Log.i("RubricRepository", e.message)
            }
        }
    }
}