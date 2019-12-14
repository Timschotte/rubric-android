package be.hogent.tile3.rubricapplication.persistence

import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.SyncStatusDao
import be.hogent.tile3.rubricapplication.model.SyncStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class NetworkStateRepository(private val syncStatusDao: SyncStatusDao) {
    init {
        App.component.inject(this)
    }
    val currentState = syncStatusDao.get()

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