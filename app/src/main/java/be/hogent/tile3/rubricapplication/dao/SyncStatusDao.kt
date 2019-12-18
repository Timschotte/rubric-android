package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import be.hogent.tile3.rubricapplication.model.SyncStatus
/**
 * Data Access Object for [SyncStatus] for Room database operations
 */
interface SyncStatusDao {
    /**
     * Inserts a [SyncStatus] in [Room] database. Exisiting [SyncStatus] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param status [SyncStatus]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: SyncStatus)

    /**
     * Retrieves [SyncStatus] from [Room] database
     * @return [LiveData] [SyncStatus]
     */
    @Query("SELECT * FROM status_table LIMIT 1")
    fun get() : LiveData<SyncStatus>

}