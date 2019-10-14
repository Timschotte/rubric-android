package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau

/**
 * NiveauDao
 */
@Dao
interface NiveauDao{
    /**
     * Retrieves all niveaus
     */
    @Query("SELECT * from niveau_table")
    fun getAllNiveaus(): LiveData<List<Niveau>>

    /**
     * Insert a niveau
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(niveau: Niveau)

    /**
     * Delete a niveau
     */
    @Delete
    fun delete(niveau: Niveau)

    /**
     * Delete all niveaus
     */
    @Query("DELETE FROM niveau_table")
    fun deleteAllNiveaus()

    @Query("SELECT * from niveau_table WHERE niveauID = :niveauId")
    fun getNiveau(niveauId: String): Niveau?

    @Query("SELECT * from niveau_table WHERE criteriumId = :criteriumId")
    fun getNiveausForCriterium(criteriumId: String): List<Niveau>
}