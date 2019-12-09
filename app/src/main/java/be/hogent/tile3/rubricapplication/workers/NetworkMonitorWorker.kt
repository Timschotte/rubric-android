package be.hogent.tile3.rubricapplication.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import be.hogent.tile3.rubricapplication.persistence.NetworkStateRepository
import javax.inject.Inject

class NetworkMonitorWorker (context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    @Inject lateinit var networkStateRepository: NetworkStateRepository
    override suspend fun doWork(): Result {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}