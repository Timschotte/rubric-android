package be.hogent.tile3.rubricapplication.persistence

import be.hogent.tile3.rubricapplication.dao.SyncStatusDao
import be.hogent.tile3.rubricapplication.model.SyncStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
/**
 * Repository for NetworkState for Room database operations
 * @constructor Creates a [NetworkStateRepository]
 * @property syncStatusDao DataAccessObject ([SyncStatusDao])for [SyncStatus]
 * @property currentState Retrieves [SyncStatus] from [Room]-database
 */
class NetworkStateRepository(private val syncStatusDao: SyncStatusDao) {
    /**
     * Properties
     */
    val currentState = syncStatusDao.get()
    /**
     * Co-Routine for inserting a [SyncStatus] in Room database. This method runs on the IO thread as a background task
     * @param active
     * @param lastSyncTime
     * @see syncStatusDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun setState(active: Boolean = true, lastSyncTime: String = ""){
        withContext(Dispatchers.IO){
            var lastSync = lastSyncTime
            if(lastSyncTime == ""){
                lastSync = Calendar.getInstance().time.toString()
            }
            syncStatusDao.insert(SyncStatus(1, active, lastSync))
        }
    }
}