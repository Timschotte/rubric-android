package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.Docent

@Dao
interface DocentDao {

    @Insert
    fun insert(docent: Docent)

    @Update
    fun update(docent: Docent)

    @Query("SELECT * from docent_table WHERE docentId = :key")
    fun getBy(key: Int): Docent

    @Query("SELECT * from docent_table ORDER BY naam")
    fun getAll(): LiveData<List<Docent>>
}