package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asDatabaseModel
import be.hogent.tile3.rubricapplication.network.asDatabaseModelArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject


/**
 * This class is used to run queries for the Rubric Objects
 */
class RubricRepository(private val rubricDao: RubricDao,
                       private val criteriumDao: CriteriumDao,
                       private val niveauDao: NiveauDao){

    @Inject lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    /**
     * Inserts a rubric in the db
     */
    @WorkerThread
    fun insert(rubric: Rubric){
        rubricDao.insert(rubric)
    }

    /**
     * Deletes a rubric from the db
     */
    @WorkerThread
    fun delete(rubric: Rubric){
        rubricDao.delete(rubric)
    }

    /**
     * Deletes all rubrics from the db
     */
    @WorkerThread
    fun deleteAllRubrics(){
        rubricDao.deleteAllRubrics()
    }

    /**
     * Retrieves all rubrics from the db
     */
    @WorkerThread
    fun getAllRubrics(): LiveData<List<Rubric>> {
        return rubricDao.getAllRubrics()
    }

    /**
     * Retrieves all rubrics from the db
     */
    @WorkerThread
    fun get(rubricId: String): LiveData<Rubric> {
        return rubricDao.getRubric(rubricId)
    }

    @WorkerThread
    fun getAllRubricsFromOpleidingsOnderdeel(id: Long): LiveData<List<Rubric>> {
        return rubricDao.getAllRubricsFromOpleidingsOnderdeel(id)
    }

    suspend fun refreshRubrics(){
        Log.i("Test", "refresh called in rubricrepository")
        try{
            withContext(Dispatchers.IO){
                val rubrics = rubricApi.getRubrics().await()
//                rubricDao.insertAll(*rubrics.asDatabaseModelArray())
                rubrics.forEach{rubric ->
                    rubricDao.insert(rubric.asDatabaseModel())
                    rubric.criteriumGroepen.forEach{ criteriumGroep ->
                        criteriumGroep.criteria.forEach{ networkCriterium ->
                            criteriumDao.insert(
                                networkCriterium.asDatabaseModel(
                                    rubric.id.toString(),
                                    criteriumGroep.id.toString()))
                            networkCriterium.criteriumNiveaus.forEach{ networkCriteriumNiveau ->
                                niveauDao.insert(
                                    networkCriteriumNiveau.asDatabaseModel(
                                        rubric.id.toString(),
                                        criteriumGroep.id.toString(),
                                        networkCriterium.id.toString()))
                            }
                        }
                    }
                    rubricDao.insert(rubric.asDatabaseModel())
                }
                rubrics.map {
                    Log.i("Test", it.omschrijving + "from refreshRubric in repository")
                }
            }

        } catch (e: IOException){
            Log.i("RubricRepository", e.message)
        }
    }

    val rubrics: LiveData<List<Rubric>> = rubricDao.getAllRubrics()
}