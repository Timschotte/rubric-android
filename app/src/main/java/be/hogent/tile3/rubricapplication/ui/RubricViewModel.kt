package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import be.hogent.tile3.rubricapplication.base.BaseViewModel
import be.hogent.tile3.rubricapplication.model.RubricResource
import be.hogent.tile3.rubricapplication.model.TestResource
import be.hogent.tile3.rubricapplication.network.ExampleRubricApi
import be.hogent.tile3.rubricapplication.network.TestApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * This viewmodel is used to retrieve a testResource from the backend
 */
class RubricViewModel : BaseViewModel() {

    /**
     * The test retrieved from the API
     */
    private val testObject = MutableLiveData<RubricResource>()



    /**
     * The instance of the TestApi class
     * to get back the results of the API
     */
    @Inject
    lateinit var testApi : ExampleRubricApi

    /**
     * Represents a disposable resources
     */
    private  var subscription: Disposable

    init {
        subscription = testApi.getTestString()
            //we tell it to fetch the data on background by
            .subscribeOn(Schedulers.io())
            //we like the fetched data to be displayed on the MainTread (UI)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveTestSucces(result) },
                { error -> onRetrieveTestError(error) }
            )

    }

    private fun onRetrieveTestError(error: Throwable) {
        Log.e("testviewmodel", error.message)
    }

    private fun onRetrieveTestSucces(result: RubricResource) {
        testObject.value = result
    }


    /**
     * Disposes the subscription when the [BaseViewModel] is no longer used.
     */
    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getTestObject(): MutableLiveData<RubricResource> {
        return testObject
    }



}