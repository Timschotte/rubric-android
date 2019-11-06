package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.Opleiding

@Dao
interface OpleidingDao {

    @Insert
    fun insert(opleiding: Opleiding)

    @Update
    fun update(opleiding: Opleiding)

    @Query("SELECT * from opleiding_table WHERE opleidingId = :key")
    fun getBy(key: Int): Opleiding

    @Query("SELECT * from opleiding_table ORDER BY naam")
    fun getAll(): LiveData<List<Opleiding>>
}