package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Evaluatie

@Dao
interface EvaluatieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(evaluatie: Evaluatie): Long

    @Update
    fun update(evaluatie: Evaluatie)

    @Query("SELECT * FROM evaluatie_table WHERE evaluatieId = :evaluatieId")
    fun get(evaluatieId: Long): Evaluatie

    @Delete
    fun delete(evaluatie: Evaluatie)
}