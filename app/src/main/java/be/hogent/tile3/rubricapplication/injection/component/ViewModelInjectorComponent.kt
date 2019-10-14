package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing the inject functions for presenters.
 */

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjectorComponent {


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjectorComponent

        fun networkModule(networkModule: NetworkModule): Builder

    }

}