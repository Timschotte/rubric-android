package be.hogent.tile3.rubricapplication.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import be.hogent.tile3.rubricapplication.persistence.EvaluatieRepository
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class NetworkPersistenceWorker (context : Context, workerParams: WorkerParameters): CoroutineWorker(context,workerParams){

    @Inject lateinit var evaluatieRepository: EvaluatieRepository

    override suspend fun doWork(): Result {
        evaluatieRepository.persistEvaluationNotSynchedToNetwork()
        return Result.success()
    }
}