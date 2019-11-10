package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Criterium

/**
 * CriteirumDao
 */
@Dao
interface CriteriumDao{
    /**
     * Retrieves all criteria
     */
    @Query("SELECT * from criterium_table")
    fun getAllCriteria(): LiveData<List<Criterium>>

    /**
     * Insert a criterium
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(criterium: Criterium)

    /**
     *  Insert list of criteria
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg criteria: Criterium)

    /**
     * Delete a criterium
     */
    @Delete
    fun delete(criterium: Criterium)

    /**
     * Delete all criterium
     */
    @Query("DELETE FROM criterium_table")
    fun deleteAllCriteria()

    @Query("SELECT * from criterium_table WHERE criteriumID = :criteriumID")
    fun getCriterium(criteriumID: String): Criterium?

    @Query("SELECT * from criterium_table WHERE rubricId = :rubricId")
    fun getCriteriaForRubric(rubricId: String): LiveData<List<Criterium>>
}