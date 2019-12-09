package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
/**
 * Data Access Object for [OpleidingsOnderdeel] for Room database operations
 * @see Dao
 */
@Dao
interface OpleidingsOnderdeelDao {
    /**
     * Inserts a [OpleidingsOnderdeel] in [Room] database
     * @param opleidingsOnderdeel [OpleidingsOnderdeel]
     */
    @Insert
    fun insert(opleidingsOnderdeel: OpleidingsOnderdeel)
    /**
     * Inserts a list of [OpleidingsOnderdeel] in [Room] database. Exisiting [OpleidingsOnderdeel] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param databaseOpleidingsOnderdeel [OpleidingsOnderdeel]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseOpleidingsOnderdeel: OpleidingsOnderdeel)

    /**
     * Updates an [OpleidingsOnderdeel] in [Room] database.
     * @param opleidingsOnderdeel [OpleidingsOnderdeel]
     */
    @Update
    fun update(opleidingsOnderdeel: OpleidingsOnderdeel)
    /**
     * Retrieves [OpleidingsOnderdeel] by id
     * @param opleidingsonderdeelId [Long]
     * @return [LiveData] [OpleidingsOnderdeel]
     */
    @Query("SELECT * from opleidingsOnderdeel_table WHERE opleidingsOnderdeelId = :opleidingsonderdeelId")
    fun getBy(opleidingsonderdeelId: Long): LiveData<OpleidingsOnderdeel>
    /**
     * Retrieves all [OpleidingsOnderdeel] from [Room] database order by naam
     * @return [LiveData] [List] of [OpleidingsOnderdeel]
     */
    @Query("SELECT * from opleidingsOnderdeel_table ORDER BY naam")
    fun getAll(): LiveData<List<OpleidingsOnderdeel>>
    /**
     * Retrieves all [OpleidingsOnderdeel] that are having Rubric from [Room] database
     * @return [LiveData] [List] of [OpleidingsOnderdeel]
     */
    @Query("SELECT * FROM opleidingsOnderdeel_table WHERE opleidingsOnderdeelId IN (SELECT DISTINCT opleidingsOnderdeelId FROM rubric_table) ORDER BY naam")
    fun getAllWithRubric(): LiveData<List<OpleidingsOnderdeel>>
}