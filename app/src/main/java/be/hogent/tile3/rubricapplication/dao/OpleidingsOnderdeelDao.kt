package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel

@Dao
interface OpleidingsOnderdeelDao {

    @Insert
    fun insert(opleidingsOnderdeel: OpleidingsOnderdeel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseOpleidingsOnderdeel: OpleidingsOnderdeel)

    @Update
    fun update(opleidingsOnderdeel: OpleidingsOnderdeel)

    @Query("SELECT * from opleidingsOnderdeel_table WHERE opleidingsOnderdeelId = :key")
    fun getBy(key: Long): LiveData<OpleidingsOnderdeel>

    @Query("SELECT * from opleidingsOnderdeel_table ORDER BY naam")
    fun getAll(): LiveData<List<OpleidingsOnderdeel>>
}