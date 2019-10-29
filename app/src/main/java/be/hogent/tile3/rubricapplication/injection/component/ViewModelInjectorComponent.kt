package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.ui.MainViewModel
import be.hogent.tile3.rubricapplication.ui.RubricViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing the inject functions for presenters.
 */

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjectorComponent {

    fun inject(mainViewModel: MainViewModel)


    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjectorComponent

        fun networkModule(networkModule: NetworkModule): Builder

    }

}