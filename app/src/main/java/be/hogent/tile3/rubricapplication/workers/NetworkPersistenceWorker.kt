package be.hogent.tile3.rubricapplication.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import be.hogent.tile3.rubricapplication.persistence.EvaluatieRepository
import be.hogent.tile3.rubricapplication.persistence.NetworkStateRepository
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class NetworkPersistenceWorker (context : Context, workerParams: WorkerParameters): Worker(context,workerParams){

    @Inject lateinit var evaluatieRepository: EvaluatieRepository
    @Inject lateinit var networkStateRepository: NetworkStateRepository

    override fun doWork(): Result {
        runBlocking { evaluatieRepository.persistEvaluationNotSynchedToNetwork() }
        runBlocking {  networkStateRepository.setState(true) }
        return Result.success()
    }
}