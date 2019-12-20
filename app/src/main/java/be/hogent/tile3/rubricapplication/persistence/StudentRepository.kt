package be.hogent.tile3.rubricapplication.persistence

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.StudentDao
import be.hogent.tile3.rubricapplication.dao.StudentOpleidingsOnderdeelDao
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asStudentDatabaseModel
import be.hogent.tile3.rubricapplication.network.asStudentOpleidingsOnderdeelDatabaseModel
import be.hogent.tile3.rubricapplication.security.AuthStateManager
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
    lateinit var context: Context
    @Inject
    lateinit var rubricApi: RubricApi
    private val authHeader: String
    /**
     * Constructor
     */
    init {
        App.component.inject(this)
        authHeader = AuthStateManager.getInstance(context).getAuthorizationHeader()
    }
    /**
     * Function for retrieving all [Student] for a given OpleidingsOnderdeel from Room database.
     * @param id ID for a given OpleidingsOnderdeel
     * @return [LiveData] [List] of [Student]
     * @see StudentOpleidingsOnderdeelDao
     */
    fun getAllStudentsFromOpleidingsOnderdeel(id: Long): LiveData<List<Student>> {
        val studenten = studentOpleidingsOnderdeelDao.getStudentenFromOpleidingsOnderdeel(id)
        return studenten
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
                val studenten = rubricApi.getStudenten(olodId, authHeader).await()
                val studentDbModels = studenten.asStudentDatabaseModel()
                val studentOpleidingDbModels = studenten.asStudentOpleidingsOnderdeelDatabaseModel()
                studentDao.insertAll(*studentDbModels)
                studentOpleidingsOnderdeelDao.insertAll(*studentOpleidingDbModels)
            }

        } catch (e: Exception){
            Log.i("RubricsLogging", "An error occured while refreshing students in database")
        }
    }
}