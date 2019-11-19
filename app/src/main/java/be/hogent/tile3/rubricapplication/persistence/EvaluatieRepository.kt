package be.hogent.tile3.rubricapplication.persistence

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class EvaluatieRepository(private val evaluatieDao: EvaluatieDao, private val criteriumEvaluatieDao: CriteriumEvaluatieDao){
    suspend fun insert(evaluatie: Evaluatie){
        return withContext(Dispatchers.IO){
            evaluatieDao.insert(evaluatie)
        }
    }

    fun insertTemp(evaluatie: Evaluatie){
            evaluatieDao.insert(evaluatie)
    }

    suspend fun update(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.update(evaluatie)
        }
    }

    suspend fun get(evaluatieId: String): Evaluatie?{
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieDao.get(evaluatieId)
            evaluatie
        }
    }

    suspend fun delete(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.delete(evaluatie)
        }
    }

    suspend fun getByRubricAndStudent(rubricId: String, studentId: Long): Evaluatie? {
        return withContext(Dispatchers.IO){
            var evaluatie = evaluatieDao.getByRubricAndStudent(rubricId, studentId)
            evaluatie
        }
    }

    suspend fun verwijderVorigeTempEvaluatie() {
            criteriumEvaluatieDao.verwijderTempCriteriumEvaluaties()
            evaluatieDao.verwijderTempEvaluatie()
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

            val checkResult = evaluatieDao.get(evaluatie.evaluatieId)
            println(checkResult?.evaluatieId)
        }



    }

    fun createTempFromBestaande(bestaandeEvaluatie: Evaluatie): Evaluatie {
        val originalId = bestaandeEvaluatie.evaluatieId

        bestaandeEvaluatie.evaluatieId = TEMP_EVALUATIE_ID
        evaluatieDao.insert(bestaandeEvaluatie)

        val criteriumEvaluaties = criteriumEvaluatieDao.getAllForEvaluatie(originalId)

        criteriumEvaluaties.forEach { it.evaluatieId = TEMP_EVALUATIE_ID }

        criteriumEvaluatieDao.insertAll(criteriumEvaluaties)

        return bestaandeEvaluatie

    }
}