package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.utils.TEMP_EVALUATIE_ID

@Dao
interface CriteriumEvaluatieDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(criteriumEvaluatie: CriteriumEvaluatie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(criteriumEvaluaties: List<CriteriumEvaluatie>)

    @Update
    fun update(criteriumEvaluatie: CriteriumEvaluatie)

    @Query("SELECT * FROM criterium_evaluatie_table WHERE criteriumEvaluatieId = :criteriumEvaluatieId")
    fun get(criteriumEvaluatieId: String): CriteriumEvaluatie

    @Query("SELECT * FROM criterium_evaluatie_table WHERE evaluatieId = :evaluatieId")
    fun getAllForEvaluatie(evaluatieId: String): List<CriteriumEvaluatie>

    @Query("SELECT * FROM criterium_evaluatie_table WHERE evaluatieId = :evaluatieId AND criteriumId = :criteriumId")
    fun getForEvaluatieAndCriterium(evaluatieId: String, criteriumId: String): CriteriumEvaluatie

    @Query("SELECT * FROM criterium_evaluatie_table")
    fun getAll(): LiveData<List<CriteriumEvaluatie>>

    @Delete
    fun delete(criteriumEvaluatie: CriteriumEvaluatie)

    @Query("DELETE FROM criterium_evaluatie_table where evaluatieId = :tempEvaluatieId")
    fun verwijderTempCriteriumEvaluaties(tempEvaluatieId: String = TEMP_EVALUATIE_ID)

    @Query("DELETE FROM criterium_evaluatie_table where evaluatieId = :evaluatieId")
    fun verwijderBestaandeCriteriumEvaluaties(evaluatieId: String)

    @Query("UPDATE criterium_evaluatie_table SET evaluatieId =:evaluatieId WHERE evaluatieId =:tempEvaluatieId ")
    fun koppelTempAanNieuw(evaluatieId: String, tempEvaluatieId: String = TEMP_EVALUATIE_ID)
}