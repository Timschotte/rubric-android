package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID

@Dao
interface EvaluatieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(evaluatie: Evaluatie): Unit

    @Update
    fun update(evaluatie: Evaluatie)

    @Query("SELECT * FROM evaluatie_table WHERE evaluatieId = :evaluatieId")
    fun get(evaluatieId: String): Evaluatie

    @Delete
    fun delete(evaluatie: Evaluatie)

    @Query("SELECT * FROM evaluatie_table where rubricId = :rubricId and studentId=:studentId and evaluatieId!=:tempEvaluatieId")
    fun getByRubricAndStudent(rubricId: Long, studentId: Long, tempEvaluatieId: String = TEMP_EVALUATIE_ID): Evaluatie

    @Query("SELECT * FROM evaluatie_table where rubricId = :rubricId and studentId=:studentId and evaluatieId=:tempEvaluatieId")
    fun getTempByRubricAndStudent(rubricId: Long, studentId: Long, tempEvaluatieId: String = TEMP_EVALUATIE_ID): Evaluatie

    @Query("DELETE FROM evaluatie_table where evaluatieId = :tempEvaluatieId")
    fun verwijderTempEvaluatie(tempEvaluatieId: String = TEMP_EVALUATIE_ID)
}