package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.injection.module.DatabaseModule
import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.persistence.*
import be.hogent.tile3.rubricapplication.ui.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class])
interface RepositoryComponent {
    fun inject(app: App)
    fun inject(criteriumOverzichtViewModel: CriteriumOverzichtViewModel)
    fun inject(rubricRepository: RubricRepository)
    fun inject(opleidingsOnderdeelViewModel: OpleidingsOnderdeelViewModel)
    fun inject(opleidingsOnderdeelRepository: OpleidingsOnderdeelRepository)
    fun inject(rubricSelectViewModel: RubricSelectViewModel)
    fun inject(leerlingSelectViewModel: LeerlingSelectViewModel)
    fun inject(studentRepository: StudentRepository)
    fun inject(evaluatieRepository: EvaluatieRepository)
    fun inject(criteriumEvaluatieRepository: CriteriumEvaluatieRepository)

    @Component.Builder
    interface Builder {
        fun build(): RepositoryComponent

        fun networkModule(networkModule: NetworkModule): Builder
        fun databaseModule(databaseModule: DatabaseModule): Builder

    }
}