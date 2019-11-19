package be.hogent.tile3.rubricapplication.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.model.Criterium

/**
 * This class is used to run queries for the Criterium Objects
 */
class CriteriumRepository(private val criteriumDao: CriteriumDao){

    /**
     * Inserts a criterium in the db
     */
    @WorkerThread
    fun insert(criterium: Criterium){
        criteriumDao.insert(criterium)
    }

    /**
     * Deletes a criterium from the db
     */
    @WorkerThread
    fun delete(criterium: Criterium){
        criteriumDao.delete(criterium)
    }

    /**
     * Deletes all criteria from the db
     */
    @WorkerThread
    fun deleteAllCriteria(){
        criteriumDao.deleteAllCriteria()
    }

    /**
     * Retrieves all criteria from the db
     */
    @WorkerThread
    fun getAllCriteria(): LiveData<List<Criterium>> {
        return criteriumDao.getAllCriteria()
    }

    @WorkerThread
    fun get(criteirumId: String): Criterium? {
        return criteriumDao.getCriterium(criteirumId)
    }

    @WorkerThread
    fun getCriteriaForRubric(rubricId: String): LiveData<List<Criterium>>{
        return criteriumDao.getCriteriaForRubric(rubricId)
    }

    fun getCriteriaListForRubric(rubricId: String): List<Criterium> {
        return criteriumDao.getCriteriaListForRubric(rubricId)
    }
}