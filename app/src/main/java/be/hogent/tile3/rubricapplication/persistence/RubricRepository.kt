package be.hogent.tile3.rubricapplication.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.Rubric


/**
 * This class is used to run queries for the Rubric Objects
 */
class RubricRepository(private val rubricDao: RubricDao){

    /**
     * Inserts a rubric in the db
     */
    @WorkerThread
    fun insert(rubric: Rubric){
        rubricDao.insert(rubric)
    }

    /**
     * Deletes a rubric from the db
     */
    @WorkerThread
    fun delete(rubric: Rubric){
        rubricDao.delete(rubric)
    }

    /**
     * Deletes all rubrics from the db
     */
    @WorkerThread
    fun deleteAllRubrics(){
        rubricDao.deleteAllRubrics()
    }

    /**
     * Retrieves all rubrics from the db
     */
    @WorkerThread
    fun getAllRubrics(): LiveData<List<Rubric>> {
        return rubricDao.getAllRubrics()
    }

    /**
     * Retrieves all rubrics from the db
     */
    @WorkerThread
    fun get(rubricId: String): Rubric? {
        return rubricDao.getRubric(rubricId)
    }
}