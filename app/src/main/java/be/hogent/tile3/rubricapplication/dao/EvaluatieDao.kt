package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import io.reactivex.Single
import kotlinx.coroutines.Deferred
/**
 * Data Access Object for [Evaluatie] for Room database operations
 * @see Dao
 */
@Dao
interface EvaluatieDao {
    /**
     * Inserts a [Evaluatie] in [Room] database. Exisiting [Evaluatie] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param evaluatie [Evaluatie]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(evaluatie: Evaluatie): Long
    /**
     * Updates an [Evaluatie] in [Room] database.
     * @param evaluatie [Evaluatie]
     */
    @Update
    fun update(evaluatie: Evaluatie)
    /**
     * Retrieves an[Evaluatie] by id
     * @param evaluatieId [String]
     * @return [Evaluatie]
     */
    @Query("SELECT * FROM evaluatie_table WHERE evaluatieId = :evaluatieId")
    fun get(evaluatieId: String): Evaluatie
    /**
     * Inserts a [List] of [Evaluatie] to Room Database
     * @return [LiveData] [List] of [Evaluatie]
     */
    @Query("SELECT * from evaluatie_table")
    fun getAll(): LiveData<List<Evaluatie>>
    /**
     * Deletes a [Evaluatie] from [Room] database
     * @param evaluatie [Evaluatie]
     */
    @Delete
    fun delete(evaluatie: Evaluatie)
    /**
     * Retrieves [Evaluatie] for a given Rubric, Student where [tempEvaluatieId] = [TEMP_EVALUATIE_ID] from [Room] database
     * @param rubricId [Long]
     * @param studentId [Long]
     * @param tempEvaluatieId [String] = [TEMP_EVALUATIE_ID]
     * @return [Evaluatie]
     */
    @Query("SELECT * FROM evaluatie_table where rubricId = :rubricId and studentId=:studentId and evaluatieId!=:tempEvaluatieId LIMIT 1")
    fun getByRubricAndStudent(rubricId: Long, studentId: Long, tempEvaluatieId: String = TEMP_EVALUATIE_ID): Evaluatie
    /**
     * Retrieves [Evaluatie] (no temporary) for a given Rubric and Student from Room database
     * @param rubricId [Long]
     * @param studentId [Long]
     * @param tempEvaluatieId [String] = [TEMP_EVALUATIE_ID]
     * @return [Evaluatie]
     */
    @Query("SELECT * FROM evaluatie_table where rubricId = :rubricId and studentId=:studentId and evaluatieId=:tempEvaluatieId LIMIT 1")
    fun getTempByRubricAndStudent(rubricId: Long, studentId: Long, tempEvaluatieId: String = TEMP_EVALUATIE_ID): Evaluatie
    /**
     * Retrieves [Evaluatie] where SyncStatus = 0 from [Room] database
     * @return [LiveData] [List] of [Evaluatie]
     */
    @Query("SELECT * FROM evaluatie_table WHERE sync=0")
    fun getNotSynched() : LiveData<List<Evaluatie>>
    /**
     * Checks if a temporary [Evaluatie] exists for a given Rubric and Student in [Room] database
     * @param rubricId [Long]
     * @param studentId [Long]
     * @param tempEvaluatieId [String] = [TEMP_EVALUATIE_ID]
     * @return [Boolean]
     */
    @Query("SELECT CASE WHEN COUNT(evaluatieId) > 0 THEN 1 ELSE 0 END FROM evaluatie_table where rubricId = :rubricId and studentId=:studentId and evaluatieId=:tempEvaluatieId")
    fun tempEvaluationExists(rubricId: Long, studentId: Long, tempEvaluatieId: String = TEMP_EVALUATIE_ID): Single<Boolean>

    /**
     * Deletes temporary [Evaluatie] from [Room] database
     * @param tempEvaluatieId [String] = [TEMP_EVALUATIE_ID]
     */
    @Query("DELETE FROM evaluatie_table where evaluatieId = :tempEvaluatieId")
    fun verwijderTempEvaluatie(tempEvaluatieId: String = TEMP_EVALUATIE_ID)
}