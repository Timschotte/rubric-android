package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Rubric

/**
 * RubricDao
 */
@Dao
interface RubricDao{
    /**
     * Retrieves all rubrics
     */
    @Query("SELECT * from rubric_table")
    fun getAllRubrics(): LiveData<List<Rubric>>

    /**
     * Insert a rubric
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rubric: Rubric)

    /**
     * Delete a rubric
     */
    @Delete
    fun delete(rubric: Rubric)

    /**
     * Delete all rubric
     */
    @Query("DELETE FROM rubric_table")
    fun deleteAllRubrics()

    @Query("SELECT * from rubric_table WHERE rubricID = :rubricId")
    fun getRubric(rubricId: String): Rubric?
}