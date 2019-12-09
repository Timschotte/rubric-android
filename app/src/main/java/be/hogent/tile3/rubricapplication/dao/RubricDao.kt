package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.EvaluatieRubric
import be.hogent.tile3.rubricapplication.model.Rubric

/**
 * Data Access Object for [Rubric] for Room database operations
 * @see Dao
 */
@Dao
interface RubricDao{
    /**
     * Retrieves all [Rubric] from [Room] database
     * @return [LiveData] list of [Rubric]
     */
    @Query("SELECT * from rubric_table")
    fun getAllRubrics(): LiveData<List<Rubric>>
    /**
     * Inserts a [Rubric] in [Room] database. Exisiting [Rubric] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param rubric [Rubric]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rubric: Rubric)
    /**
     * Inserts a list of [Rubric] in [Room] database. Exisiting [Rubric] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param databaseRubrics [Rubric]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseRubrics: Rubric)
    /**
     * Inserts a list of [Rubric] in [Room] database. Exisiting [Rubric] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param rubrics [List] of [Rubric]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRubrics(rubrics: List<Rubric>)
    /**
     * Deletes a [Rubric] from [Room] database
     * @param rubric [Rubric]
     */
    @Delete
    fun delete(rubric: Rubric)
    /**
     * Delete all [Rubric] from [Room] database
     */
    @Query("DELETE FROM rubric_table")
    fun deleteAllRubrics()
    /**
     * Retrieves [Rubric] by id from [Room] database
     * @param rubricId [String]
     * @return [LiveData] [Rubric]
     */
    @Query("SELECT * from rubric_table WHERE rubricID = :rubricId")
    fun getRubric(rubricId: String): LiveData<Rubric>
    /**
     * Retrieves all [Rubric] for a given OpleidingsOnderdeel from [Room] database
     * @param opleidingsOnderdeelId [Long]
     * @return [LiveData] [List] of [Rubric]
     */
    @Query("SELECT * from rubric_table WHERE opleidingsOnderdeelId = :opleidingsOnderdeelId")
    fun getAllRubricsFromOpleidingsOnderdeel(opleidingsOnderdeelId: Long): LiveData<List<Rubric>>
    /**
     * Retrieves [EvaluatieRubric] for a given [Rubric] from [Room] database
     * @param rubricId [Long]
     * @return [EvaluatieRubric]
     */
    @Transaction
    @Query("SELECT * FROM rubric_table WHERE rubricId = :rubricId")
    fun getEvaluatieRubric(rubricId: Long): EvaluatieRubric
}