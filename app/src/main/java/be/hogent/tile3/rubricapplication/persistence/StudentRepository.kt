package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.StudentDao
import be.hogent.tile3.rubricapplication.dao.StudentOpleidingsOnderdeelDao
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asStudentDatabaseModel
import be.hogent.tile3.rubricapplication.network.asStudentOpleidingsOnderdeelDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
/**
 * Repository for [Student] for Room database operations
 * @constructor Creates a [StudentRepository]
 * @property studentDao DataAccessObject ([StudentDao]) for [Student]
 * @property studentOpleidingsOnderdeelDao DataAccessObject ([StudentOpleidingsOnderdeelDao])for StudentOpledingsOnderdeel
 * @property rubricApi API for backend communication
 */
class StudentRepository(private val studentDao: StudentDao, private val studentOpleidingsOnderdeelDao: StudentOpleidingsOnderdeelDao) {
    /**
     * Properties
     */
    @Inject
    lateinit var rubricApi: RubricApi
    /**
     * Constructor
     */
    init {
        App.component.inject(this)
    }
    /**
     * Function for retrieving all [Student] for a given OpleidingsOnderdeel from Room database.
     * @param id ID for a given OpleidingsOnderdeel
     * @return [LiveData] [List] of [Student]
     * @see StudentOpleidingsOnderdeelDao
     */
    fun getAllStudentsFromOpleidingsOnderdeel(id: Long): LiveData<List<Student>> {
        return studentOpleidingsOnderdeelDao.getStudentenFromOpleidingsOnderdeel(id)
    }
    /**
     * Co-Routine for synchronizing all [Student] for a given OpleidingsOnderdeel from backend API with Room database.
     * @param olodId ID for a given OpleidingsOnderdeel
     * @see RubricApi
     * @see StudentOpleidingsOnderdeelDao
     * @see StudentDao
     * @see withContext
     * @see Dispatchers.IO
     */
    suspend fun refreshStudenten(olodId : Long){
        try{
            withContext(Dispatchers.IO){
                val studenten = rubricApi.getStudenten(olodId).await()
                studentDao.insertAll(*studenten.asStudentDatabaseModel())
                studentOpleidingsOnderdeelDao.insertAll(*studenten.asStudentOpleidingsOnderdeelDatabaseModel())
            }

        } catch (e: IOException){
            Log.i("StudentRepository", e.message)
        }
    }
}