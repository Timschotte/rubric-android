package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel

@Dao
interface OpleidingsOnderdeelDao {

    @Insert
    fun insert(opleidingsOnderdeel: OpleidingsOnderdeel)

    @Update
    fun update(opleidingsOnderdeel: OpleidingsOnderdeel)

    @Query("SELECT * from opleidingsOnderdeel_table WHERE opleidingsOnderdeelId = :key")
    fun getBy(key: Int): OpleidingsOnderdeel

    @Query("SELECT * from opleidingsOnderdeel_table ORDER BY naam")
    fun getAll(): LiveData<List<OpleidingsOnderdeel>>
}