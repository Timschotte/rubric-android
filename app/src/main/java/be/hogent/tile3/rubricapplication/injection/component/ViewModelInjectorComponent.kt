package be.hogent.tile3.rubricapplication.injection.component

import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.ui.RubricViewModel
import be.hogent.tile3.rubricapplication.ui.TestViewModel
import dagger.Component
import javax.inject.Singleton


/**
 * Component providing the inject functions for presenters.
 */

@Singleton
@Component(modules = [NetworkModule::class])
interface ViewModelInjectorComponent {


    /**
     * Injects the dependencies into the specified TestViewModel.
     * @param testViewModel the [TestViewModel] in which to inject the dependencies.
     */
    fun inject(testViewModel: TestViewModel)

    fun inject(rubricViewModel : RubricViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjectorComponent

        fun networkModule(networkModule: NetworkModule): Builder

    }

}