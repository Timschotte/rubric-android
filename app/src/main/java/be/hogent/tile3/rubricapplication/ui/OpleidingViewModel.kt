package be.hogent.tile3.rubricapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import be.hogent.tile3.rubricapplication.dao.OpleidingDao
import be.hogent.tile3.rubricapplication.model.Opleiding
import kotlinx.coroutines.*


class OpleidingViewModel(val database: OpleidingDao,
                         application: Application): AndroidViewModel(application) {


    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var opleidingen = database.getAll()

    init {


        System.out.println("Dit zijn de opleidingen")
        System.out.println(opleidingen.toString())
            //initializeDatabase()


    }


    private fun initializeDatabase(){
        uiScope.launch {

            val opleiding1 = Opleiding(4, "test")
            val opleiding2 = Opleiding(5, "test2")
            val opleiding3 = Opleiding(6, "test3")

            insert(opleiding1)
            insert(opleiding2)
            insert(opleiding3)

        }
    }

    private suspend fun insert(opleiding: Opleiding) {
        withContext(Dispatchers.IO){
            database.insert(opleiding)
        }
    }

}