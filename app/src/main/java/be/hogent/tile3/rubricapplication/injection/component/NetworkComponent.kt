package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.ui.MainViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing the inject functions for presenters.
 */

@Singleton
@Component(modules = [NetworkModule::class])
interface NetworkComponent {

    fun inject(mainViewModel: MainViewModel)


    @Component.Builder
    interface Builder {
        fun build(): NetworkComponent

        fun networkModule(networkModule: NetworkModule): Builder

    }

}