package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
/**
 * Data Access Object for [CriteriumEvaluatie] for Room database operations
 * @see Dao
 */
@Dao
interface CriteriumEvaluatieDao{

    /**
     * Inserts a [CriteriumEvaluatie] in [Room] database. Exisiting [CriteriumEvaluatie] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param criteriumEvaluatie [CriteriumEvaluatie]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(criteriumEvaluatie: CriteriumEvaluatie)
    /**
     * Inserts a list of [CriteriumEvaluatie] in [Room] database. Exisiting [CriteriumEvaluatie] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param criteriumEvaluaties [List] of [CriteriumEvaluatie]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(criteriumEvaluaties: List<CriteriumEvaluatie>)
    /**
     * Updates a [CriteriumEvaluatie] in [Room] database
     * @param criteriumEvaluatie [CriteriumEvaluatie]
     */
    @Update
    fun update(criteriumEvaluatie: CriteriumEvaluatie)
    /**
     * Retrieves [CriteriumEvaluatie] by id
     * @param criteriumEvaluatieId [String]
     * @return [CriteriumEvaluatie]
     */
    @Query("SELECT * FROM criterium_evaluatie_table WHERE criteriumEvaluatieId = :criteriumEvaluatieId")
    fun get(criteriumEvaluatieId: String): CriteriumEvaluatie
    /**
     * Retrieves all [CriteriumEvaluatie] for a given Evaluatie
     * @param evaluatieId [String]
     * @return [List] of [CriteriumEvaluatie]
     */
    @Query("SELECT * FROM criterium_evaluatie_table WHERE evaluatieId = :evaluatieId")
    fun getAllForEvaluatie(evaluatieId: String): List<CriteriumEvaluatie>
    /**
     * Retrieves a [CriteriumEvaluatie] for a given Evaluatie and Criterium
     * @param evaluatieId [String]
     * @param criteriumId [String]
     * @return [CriteriumEvaluatie]
     */
    @Query("SELECT * FROM criterium_evaluatie_table WHERE evaluatieId = :evaluatieId AND criteriumId = :criteriumId")
    fun getForEvaluatieAndCriterium(evaluatieId: String, criteriumId: String): CriteriumEvaluatie
    /**
     * Retrieves all [CriteriumEvaluatie] from [Room] database
     * @return [LiveData] [List] of [CriteriumEvaluatie]
     */
    @Query("SELECT * FROM criterium_evaluatie_table")
    fun getAll(): LiveData<List<CriteriumEvaluatie>>
    /**
     * Deletes a [CriteriumEvaluatie] from [Room] database
     * @param criteriumEvaluatie [CriteriumEvaluatie]
     */
    @Delete
    fun delete(criteriumEvaluatie: CriteriumEvaluatie)
    /**
     * Deletes a [CriteriumEvaluatie] by a given Evaluatie from [Room] database
     * @param evaluatieId [String]
     */
    @Query("DELETE FROM criterium_evaluatie_table where evaluatieId = :evaluatieId")
    fun verwijderBestaandeCriteriumEvaluaties(evaluatieId: String)

    /**
     * Updates a [CriteriumEvaluatie] for a given Evaluatie
     * @param evaluatieId [String]
     * @param tempEvaluatieId [String] = [TEMP_EVALUATIE_ID]
     */
    @Query("UPDATE criterium_evaluatie_table SET evaluatieId =:evaluatieId WHERE evaluatieId =:tempEvaluatieId ")
    fun koppelTempAanNieuw(evaluatieId: String, tempEvaluatieId: String = TEMP_EVALUATIE_ID)
}