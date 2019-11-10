package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.CriteriumDao
import be.hogent.tile3.rubricapplication.dao.NiveauDao
import be.hogent.tile3.rubricapplication.dao.RubricDao
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asCriteriumDatabaseModelArray
import be.hogent.tile3.rubricapplication.network.asDatabaseModelArray
import be.hogent.tile3.rubricapplication.network.asNiveauDatabaseModelArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject


/**
 * This class is used to run queries for the Rubric Objects
 */
class RubricRepository(private val rubricDao: RubricDao){

    @Inject lateinit var rubricApi: RubricApi
    @Inject lateinit var criteriumDao: CriteriumDao
    @Inject lateinit var niveauDao: NiveauDao

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
    fun get(rubricId: String): Rubric? {
        return rubricDao.getRubric(rubricId)
    }

    suspend fun refreshRubrics(){
        Log.i("Test", "refresh called in rubricrepository")
        try{
            val rubrics = rubricApi.getRubrics().await()
            withContext(Dispatchers.IO){
                rubricDao.insertAll(*rubrics.asDatabaseModelArray())
                criteriumDao.insertAll(*rubrics.asCriteriumDatabaseModelArray())
                niveauDao.insertAll(*rubrics.asNiveauDatabaseModelArray())

            }
            rubrics.map {
                Log.i("Test", it.omschrijving + "from refreshRubric in repository")
            }
        } catch (e: IOException){
            Log.i("RubricRepository", e.message)
        }
    }

    val rubrics: LiveData<List<Rubric>> = rubricDao.getAllRubrics()
    val criteria: LiveData<List<Criterium>> = criteriumDao.getAllCriteria()
    val niveaus: LiveData<List<Niveau>> = niveauDao.getAllNiveaus()
}