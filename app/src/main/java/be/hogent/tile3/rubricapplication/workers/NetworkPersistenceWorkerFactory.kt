package be.hogent.tile3.rubricapplication.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import be.hogent.tile3.rubricapplication.persistence.EvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.NetworkStateRepository

class NetworkPersistenceWorkerFactory(private val evaluatieRepository: EvaluatieRepository, private val networkStateRepository: NetworkStateRepository) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerKlass = Class.forName(workerClassName).asSubclass(Worker::class.java)
        val constructor =
            workerKlass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
        val instance = constructor.newInstance(appContext, workerParameters)

        when (instance) {
            is NetworkPersistenceWorker -> {
                instance.evaluatieRepository = evaluatieRepository
                instance.networkStateRepository = networkStateRepository
            }
        }

        return instance
    }
}