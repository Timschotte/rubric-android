package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.Docent
/**
 * Data Access Object for [Docent] for Room database operations
 * @see Dao
 */
@Dao
interface DocentDao {
    /**
     * Inserts a [Docent] in [Room] database.
     * @param docent [Docent]
     */
    @Insert
    fun insert(docent: Docent)
    /**
     * Updates a [Docent] in [Room] database.
     * @param docent [Docent]
     */
    @Update
    fun update(docent: Docent)
    /**
     * Retrieves [Docent] by id
     * @param docentId [Int]
     * @return [Docent]
     */
    @Query("SELECT * from docent_table WHERE docentId = :docentId")
    fun getBy(docentId: Int): Docent
    /**
     * Retrieves all [Docent] from [Room] database ordered by [naam]
     * @return [LiveData] list of [Docent]
     */
    @Query("SELECT * from docent_table ORDER BY naam")
    fun getAll(): LiveData<List<Docent>>
}