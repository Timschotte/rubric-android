package be.hogent.tile3.rubricapplication

import android.app.Application
import be.hogent.tile3.rubricapplication.injection.component.DaggerDatabaseComponent
import be.hogent.tile3.rubricapplication.injection.component.DatabaseComponent
import be.hogent.tile3.rubricapplication.injection.module.DatabaseModule
import javax.inject.Inject

/**
 * This is the applicationContext used in the application
 */
class App : Application() {
    companion object {
        lateinit var component: DatabaseComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerDatabaseComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .build()
    }
}