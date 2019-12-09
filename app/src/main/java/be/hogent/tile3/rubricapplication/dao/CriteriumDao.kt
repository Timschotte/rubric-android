package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Criterium

/**
 * Data Access Object for [Criterium] for Room database operations
 * @see Dao
 */
@Dao
interface CriteriumDao{
    /**
     * Retrieves all [Criterium] from Room database
     * @return [LiveData] list of [Criterium]
     */
    @Query("SELECT * from criterium_table")
    fun getAllCriteria(): LiveData<List<Criterium>>
    /**
     * Inserts a [Criterium] in Room database. Exisiting [Criterium] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param criterium [Criterium]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(criterium: Criterium)
    /**
     * Inserts a list of [Criterium] in Room database. Exisiting [Criterium] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param databaseCriteria [Criterium]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseCriteria: Criterium)
    /**
     * Deletes a [Criterium] from Room database
     * @param criterium [Criterium]
     */
    @Delete
    fun delete(criterium: Criterium)
    /**
     * Deletes all [Criterium] from Room database
     */
    @Query("DELETE FROM criterium_table")
    fun deleteAllCriteria()
    /**
     * Retrieves [Criterium] by id from Room database
     * @param criteriumID [String]
     * @return [Criterium]. Is null if no matching [Criterium] is found
     */
    @Query("SELECT * from criterium_table WHERE criteriumID = :criteriumID")
    fun getCriterium(criteriumID: String): Criterium?
    /**
     * Retrieves list of [Criterium] for a given Rubric from Room database
     * @param rubricId [String]
     * @return [LiveData] list of [Criterium]
     */
    @Query("SELECT * from criterium_table WHERE rubricId = :rubricId")
    fun getCriteriaForRubric(rubricId: String): LiveData<List<Criterium>>
    /**
     * Retrieves list of [Criterium] for a Rubric from Room database
     * @param rubricId [String]
     * @return [LiveData] list of [Criterium]
     */
    @Query("SELECT * from criterium_table WHERE rubricId = :rubricId")
    fun getCriteriaListForRubric(rubricId: String): List<Criterium>
}