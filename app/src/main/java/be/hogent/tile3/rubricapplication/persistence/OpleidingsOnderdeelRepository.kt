package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.OpleidingsOnderdeelDao
import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asOpleidingsOnderdeelDatabaseModel
import java.io.IOException
import javax.inject.Inject
/**
 * Repository for [OpleidingsOnderdeel] for Room database operations
 * @constructor Creates a [OpleidingsOnderdeelRepository]
 * @property opleidingsOnderdeelDao DataAccessObject ([OpleidingsOnderdeelDao])for [OpleidingsOnderdeel]
 * @property rubricApi API for backend communication
 * @property opleidingsOnderdelen Retrieves [LiveData][List] of [OpleidingsOnderdeel] from Room database
 */
class OpleidingsOnderdeelRepository(private val opleidingsOnderdeelDao: OpleidingsOnderdeelDao) {

    /**
     * Properties
     */
    @Inject lateinit var context: Context
    @Inject lateinit var rubricApi: RubricApi
    val opleidingsOnderdelen: LiveData<List<OpleidingsOnderdeel>> = opleidingsOnderdeelDao.getAll()

    /**
     * Constructor
     */
    init {
        App.component.inject(this)
    }
    /**
     * Function for retrieving an [OpleidingsOnderdeel] from Room database.
     * @param id ID for [OpleidingsOnderdeel] to be retrieved
     * @return [LiveData] [OpleidingsOnderdeel]
     */
    fun get(id: Long): LiveData<OpleidingsOnderdeel> {
        return opleidingsOnderdeelDao.getBy(id)
    }
    /**
     * Function for retrieving all [OpleidingsOnderdeel] from backend API and inserting in Room database
     * @see RubricApi
     * @see OpleidingsOnderdeelDao
     */
    suspend fun refreshOpleidingsOnderdelen(){
        try{
            val opleidingsOnderdelen = rubricApi.getOpleidingsOnderdeel().await()
            opleidingsOnderdeelDao.insertAll(*opleidingsOnderdelen.asOpleidingsOnderdeelDatabaseModel())
        } catch (e: IOException){
            Log.i("RubricsLogging","An error occured while refreshing olods in database")
        }
    }
    /**
     * Function for retrieving all [OpleidingsOnderdeel] that have Rubric from Room database.
     * @return [LiveData] [List] of [OpleidingsOnderdeel]
     * @see OpleidingsOnderdeelDao
     */
    fun getAllOpleidingsOnderdelenWithRubric(): LiveData<List<OpleidingsOnderdeel>> {
        val opleidingsOnderdelen = opleidingsOnderdeelDao.getAllWithRubric()
        return opleidingsOnderdelen
    }

}