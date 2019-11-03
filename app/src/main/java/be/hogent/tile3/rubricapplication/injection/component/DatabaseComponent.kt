package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.injection.module.DatabaseModule
import be.hogent.tile3.rubricapplication.ui.*
import dagger.Component
import javax.inject.Singleton

/**
 * This will inject the databasecomponent into our MainViewModel
 */
@Singleton
@Component(modules = [DatabaseModule::class])
interface DatabaseComponent {
    fun inject(app: App)
    fun inject(rubricViewModel: RubricViewModel)
    fun inject(criteriumViewModel: CriteriumViewModel)
    fun inject(niveauViewModel: NiveauViewModel)
    fun inject(criteriumOverzichtViewModel: CriteriumOverzichtViewModel)
    fun inject(criteriumEvaluatieViewModel: CriteriumEvaluatieViewModel)
}