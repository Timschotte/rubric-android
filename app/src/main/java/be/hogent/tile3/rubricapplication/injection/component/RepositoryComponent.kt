package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.injection.module.DatabaseModule
import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.persistence.RubricRepository
import be.hogent.tile3.rubricapplication.ui.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface RepositoryComponent {
    fun inject(app: App)
    fun inject(rubricViewModel: RubricViewModel)
    fun inject(criteriumViewModel: CriteriumViewModel)
    fun inject(niveauViewModel: NiveauViewModel)
    fun inject(criteriumOverzichtViewModel: CriteriumOverzichtViewModel)
    fun inject(criteriumEvaluatieViewModel: CriteriumEvaluatieViewModel)
    fun inject(rubricRepository: RubricRepository)

    @Component.Builder
    interface Builder {
        fun build(): RepositoryComponent

        fun networkModule(networkModule: NetworkModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder

    }
}