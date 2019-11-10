package be.hogent.tile3.rubricapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.data.RubricData
import be.hogent.tile3.rubricapplication.network.RubricApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel: ViewModel(){
    private val rubricDataObject = MutableLiveData<List<RubricData>>()

    @Inject
    lateinit var rubricApi: RubricApi

    private var subscription: Disposable

    init {

        App.component.inject(this)

        subscription = rubricApi.getRubrics()
            //we tell it to fetch the data on background by
            .subscribeOn(Schedulers.io())
            //we like the fetched data to be displayed on the MainTread (UI)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> onRetrieveRubricSucces(result) },
                { error -> onRetrieveRubricError(error) }
            )

    }

    private fun onRetrieveRubricError(error: Throwable) {
        Log.e("mainviewmodel", error.message)
    }

    private fun onRetrieveRubricSucces(result: List<RubricData>) {
        rubricDataObject.value = result
    }


    /**
     * Disposes the subscription when the [BaseViewModel] is no longer used.
     */
    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }

    fun getRubricDataObject(): MutableLiveData<List<RubricData>> {
        return rubricDataObject
    }


}