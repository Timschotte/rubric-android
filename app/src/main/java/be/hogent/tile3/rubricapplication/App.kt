package be.hogent.tile3.rubricapplication

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import be.hogent.tile3.rubricapplication.injection.component.DaggerRepositoryComponent
import be.hogent.tile3.rubricapplication.injection.component.RepositoryComponent
import be.hogent.tile3.rubricapplication.injection.module.DatabaseModule
import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.workers.NetworkPersistenceWorkerFactory
import javax.inject.Inject

/**
 * This is the applicationContext used in the application
 */
class App : Application() {
    companion object {
        lateinit var component: RepositoryComponent
    }


    override fun onCreate() {
        super.onCreate()
        component = DaggerRepositoryComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .networkModule(NetworkModule())
            .build()
    }

}