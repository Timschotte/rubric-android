package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.OpleidingsOnderdeelDao
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asOpleidingsOnderdeelDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class OpleidingsOnderdeelRepository(private val opleidingsOnderdeelDao: OpleidingsOnderdeelDao) {

    @Inject lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    fun get(id: Long): LiveData<OpleidingsOnderdeel> {
        return opleidingsOnderdeelDao.getBy(id)
    }

    suspend fun refreshOpleidingsOnderdelen(){
        Log.i("Test", "refresh called in opleidingsOnderdeelRepo")
        try{
                val opleidingsOnderdelen = rubricApi.getOpleidingsOnderdeel().await()

                opleidingsOnderdeelDao.insertAll(*opleidingsOnderdelen.asOpleidingsOnderdeelDatabaseModel())
                opleidingsOnderdelen.map {
                    Log.i("Test", it.naam + "from refreshRubric in repository")
                }


        } catch (e: IOException){
            Log.i("RubricRepository", e.message)
        }
    }

    fun getAllOpleidingsOnderdelenWithRubric(): LiveData<List<OpleidingsOnderdeel>> {
        val opleidingsOnderdelen = opleidingsOnderdeelDao.getAllWithRubric()
        return opleidingsOnderdelen
    }

    val opleidingsOnderdelen: LiveData<List<OpleidingsOnderdeel>> = opleidingsOnderdeelDao.getAll()

}