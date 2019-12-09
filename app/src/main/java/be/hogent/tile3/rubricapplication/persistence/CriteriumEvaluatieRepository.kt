package be.hogent.tile3.rubricapplication.persistence

import be.hogent.tile3.rubricapplication.dao.CriteriumEvaluatieDao
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for [CriteriumEvaluatie] for Room database operations
 * @constructor Creates a [CriteriumEvaluatieRepository]
 * @property criteriumEvaluatieDao DataAccessObject ([CriteriumEvaluatieDao])for [CriteriumEvaluatie]
 */
class CriteriumEvaluatieRepository(private val criteriumEvaluatieDao: CriteriumEvaluatieDao) {
    /**
     * Co-Routine for inserting a [CriteriumEvaluatie] in Room database. This method runs on the IO thread as a background task
     * @param criteriumEvaluatie [CriteriumEvaluatie] to be inserted
     * @see CriteriumEvaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun insert(criteriumEvaluatie: CriteriumEvaluatie) {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.insert(criteriumEvaluatie)
        }
    }
    /**
     * Co-Routine for inserting a list of [CriteriumEvaluatie] in Room database. This method runs on the IO thread as a background task
     * @param criteriumEvaluaties [List] of [CriteriumEvaluatie] to be inserted
     * @see CriteriumEvaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun insertAll(criteriumEvaluaties: List<CriteriumEvaluatie>) {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.insertAll(criteriumEvaluaties)
        }
    }
    /**
     * Co-Routine for updating a [CriteriumEvaluatie] in Room database. This method runs on the IO thread as a background task
     * @param criteriumEvaluatie [CriteriumEvaluatie] to be updated
     * @see CriteriumEvaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun update(criteriumEvaluatie: CriteriumEvaluatie) {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.update(criteriumEvaluatie)
        }
    }
    /**
     * Co-Routine for retrieving a [CriteriumEvaluatie] from Room database. This method runs on the IO thread as a background task
     * @param criteriumEvaluatieId ID from [CriteriumEvaluatie] to be retrieved
     * @return [CriteriumEvaluatie]
     * @see CriteriumEvaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun get(criteriumEvaluatieId: String): CriteriumEvaluatie {
        return withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.get(criteriumEvaluatieId)
        }
    }
    /**
     * Co-Routine for retrieving all [CriteriumEvaluatie] from a given [Evaluatie] from Room database. This method runs on the IO thread as a background task
     * @param evaluatieId ID from [Evaluatie] to be retrieved
     * @return [List] of [CriteriumEvaluatie]
     * @see CriteriumEvaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun getAllForEvaluatie(evaluatieId: String): List<CriteriumEvaluatie> {
        return withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.getAllForEvaluatie(evaluatieId)
        }
    }
    /**
     * Co-Routine for deleting a [CriteriumEvaluatie] from Room database. This method runs on the IO thread as a background task
     * @param criteriumEvaluatie [CriteriumEvaluatie] to be deleted
     * @see CriteriumEvaluatieDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun delete(criteriumEvaluatie: CriteriumEvaluatie) {
        withContext(Dispatchers.IO) {
            criteriumEvaluatieDao.delete(criteriumEvaluatie)
        }
    }
}