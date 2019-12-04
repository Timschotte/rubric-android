package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.SyncStatus

interface SyncStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: SyncStatus)

    @Query("SELECT * FROM status_table LIMIT 1")
    fun get() : LiveData<SyncStatus>

}