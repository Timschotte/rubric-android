package be.hogent.tile3.rubricapplication.base

import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.injection.component.DaggerViewModelInjectorComponent
import be.hogent.tile3.rubricapplication.injection.component.ViewModelInjectorComponent
import be.hogent.tile3.rubricapplication.injection.module.NetworkModule
import be.hogent.tile3.rubricapplication.ui.TestViewModel

/**
 * A wrapper for our viewmodels that injects the networkdependency
 */
abstract class BaseViewModel : ViewModel() {

    private val injectorComponent: ViewModelInjectorComponent = DaggerViewModelInjectorComponent
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is TestViewModel -> injectorComponent.inject(this)
        }
    }

}