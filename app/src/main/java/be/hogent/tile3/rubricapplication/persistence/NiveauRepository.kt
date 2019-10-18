package be.hogent.tile3.rubricapplication.persistence

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.model.Niveau

/**
 * This class is used to run queries for the Niveau Objects
 */
class NiveauRepository(private val niveauDao: NiveauDao){

    /**
     * Inserts a niveau in the db
     */
    @WorkerThread
    fun insert(niveau: Niveau){
        niveauDao.insert(niveau)
    }

    /**
     * Deletes a niveau from the db
     */
    @WorkerThread
    fun delete(niveau: Niveau){
        niveauDao.delete(niveau)
    }

    /**
     * Deletes all niveaus from the db
     */
    @WorkerThread
    fun deleteAllNiveaus(){
        niveauDao.deleteAllNiveaus()
    }

    /**
     * Retrieves all niveaus from the db
     */
    @WorkerThread
    fun getAllNiveaus(): LiveData<List<Niveau>> {
        return niveauDao.getAllNiveaus()
    }

    @WorkerThread
    fun get(niveauId: String): Niveau? {
        return niveauDao.getNiveau(niveauId)
    }

    @WorkerThread
    fun getNiveausForCriterium(criteriumId: String): List<Niveau>{
        return niveauDao.getNiveausForCriterium(criteriumId)
    }

    @WorkerThread
    fun getNiveausForRubric(rubricId: String): List<Niveau>{
        return niveauDao.getNiveausForRubric(rubricId)
    }
}