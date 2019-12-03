package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Evaluatie
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID
import io.reactivex.Single
import kotlinx.coroutines.Deferred

@Dao
interface EvaluatieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(evaluatie: Evaluatie)

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

    @Query("SELECT * FROM evaluatie_table WHERE sync=0")
    fun getNotSynched() : LiveData<List<Evaluatie>>

    @Query("SELECT CASE WHEN COUNT(evaluatieId) > 0 THEN 1 ELSE 0 END FROM evaluatie_table where rubricId = :rubricId and studentId=:studentId and evaluatieId=:tempEvaluatieId")
    fun tempEvaluationExists(rubricId: Long, studentId: Long, tempEvaluatieId: String = TEMP_EVALUATIE_ID): Single<Boolean>

    @Query("DELETE FROM evaluatie_table where evaluatieId = :tempEvaluatieId")
    fun verwijderTempEvaluatie(tempEvaluatieId: String = TEMP_EVALUATIE_ID)
}