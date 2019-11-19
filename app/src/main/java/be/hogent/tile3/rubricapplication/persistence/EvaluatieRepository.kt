package be.hogent.tile3.rubricapplication.persistence

import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.Evaluatie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        withContext(Dispatchers.IO){
            criteriumEvaluatieDao.verwijderTempCriteriumEvaluaties()
            evaluatieDao.verwijderTempEvaluatie()
        }
    }
}