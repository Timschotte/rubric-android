package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CriteriumEvaluatieRepository(private val criteriumEvaluatieDao: CriteriumEvaluatieDao) {
    val criteriumEvaluaties = criteriumEvaluatieDao.getAll()

    suspend fun insert(criteriumEvaluatie: CriteriumEvaluatie) {
        withContext(Dispatchers.IO) {
            Log.i(
                "CriteriumEvaluatieRepo",
                "Inserting criteval " + criteriumEvaluatie.criteriumEvaluatieId
            )
            criteriumEvaluatieDao.insert(criteriumEvaluatie)
        }
    }

    suspend fun insertAll(criteriumEvaluaties: List<CriteriumEvaluatie>) {
        Log.i(
            "CriteriumEvaluatieRepo",
            "Inserting " + criteriumEvaluaties.size + " criteriumEvaluaties"
        )
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.insertAll(criteriumEvaluaties)
        }
    }

    fun insertAllTemp(criteriumEvaluaties: List<CriteriumEvaluatie>) {
        Log.i(
            "CriteriumEvaluatieRepo",
            "Inserting " + criteriumEvaluaties.size + " criteriumEvaluaties"
        )
        criteriumEvaluatieDao.insertAll(criteriumEvaluaties)

    }

    suspend fun update(criteriumEvaluatie: CriteriumEvaluatie) {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.update(criteriumEvaluatie)
        }
    }

    suspend fun get(criteriumEvaluatieId: String): CriteriumEvaluatie {
        return withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.get(criteriumEvaluatieId)
        }
    }

    suspend fun getAllForEvaluatie(evaluatieId: String): List<CriteriumEvaluatie> {
        return withContext(Dispatchers.IO) {
            var criteriumEvaluaties = criteriumEvaluatieDao.getAllForEvaluatie(evaluatieId)
            criteriumEvaluaties
        }
    }

    suspend fun getForEvaluatieAndCriterium(evaluatieId: String, criteriumId: String):
            CriteriumEvaluatie {
        return withContext(Dispatchers.IO) {
            var criteriumEvaluatie =
                criteriumEvaluatieDao.getForEvaluatieAndCriterium(evaluatieId, criteriumId)
            criteriumEvaluatie
        }
    }

    suspend fun delete(criteriumEvaluatie: CriteriumEvaluatie) {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.delete(criteriumEvaluatie)
        }
    }
}