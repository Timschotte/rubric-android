package be.hogent.tile3.rubricapplication.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.SyncStatus

interface SyncStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: SyncStatus)

    @Update
    fun update(status: SyncStatus)

    @Query("SELECT * FROM status_table ")
    fun get()

}