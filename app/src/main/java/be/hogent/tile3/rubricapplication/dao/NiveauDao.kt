package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Niveau

/**
 * Data Access Object for [Niveau] for Room database operations
 * @see Dao
 */
@Dao
interface NiveauDao{
    /**
     * Retrieves all [Niveau] from [Room] database
     * @return [LiveData] list of [Niveau]
     */
    @Query("SELECT * from niveau_table")
    fun getAllNiveaus(): LiveData<List<Niveau>>
    /**
     * Inserts a [Niveau] in [Room] database. Exisiting [Niveau] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param niveau [Niveau]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(niveau: Niveau)
    /**
     * Deletes a [Niveau] from [Room] database
     * @param niveau [Niveau]
     */
    @Delete
    fun delete(niveau: Niveau)
    /**
     * Delete all niveaus
     */
    @Query("DELETE FROM niveau_table")
    fun deleteAllNiveaus()
    /**
     * Retrieves [Niveau] by id
     * @param niveauId [String]
     * @return [Niveau]. Is null if no matching [Niveau] is found
     */
    @Query("SELECT * from niveau_table WHERE niveauID = :niveauId")
    fun getNiveau(niveauId: String): Niveau?
    /**
     * Retrieves [Niveau] for a given Criterium from [Room] database
     * @param criteriumId [String]
     * @return [List] of [Niveau]
     */
    @Query("SELECT * from niveau_table WHERE criteriumId = :criteriumId")
    fun getNiveausForCriterium(criteriumId: String): List<Niveau>
    /**
     * Retrieves [Niveau] for a given Rubric from [Room] database
     * @param rubricId [String]
     * @return [List] of [Niveau]
     */
    @Query("SELECT niveau_table.* from niveau_table JOIN criterium_table ON niveau_table.criteriumId = criterium_table.criteriumId WHERE criterium_table.rubricId = :rubricId")
    fun getNiveausForRubric(rubricId: String): List<Niveau>
}