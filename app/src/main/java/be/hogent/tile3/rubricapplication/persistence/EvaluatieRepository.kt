package be.hogent.tile3.rubricapplication.persistence

import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.EvaluatieDao
import be.hogent.tile3.rubricapplication.model.Evaluatie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EvaluatieRepository(private val evaluatieDao: EvaluatieDao){
    suspend fun insert(evaluatie: Evaluatie): Long{
        return withContext(Dispatchers.IO){
            var id: Long = evaluatieDao.insert(evaluatie)
            id
        }
    }

    suspend fun update(evaluatie: Evaluatie){
        withContext(Dispatchers.IO){
            evaluatieDao.update(evaluatie)
        }
    }

    suspend fun get(evaluatieId: Long): Evaluatie{
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
}