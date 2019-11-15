package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import be.hogent.tile3.rubricapplication.App
import be.hogent.tile3.rubricapplication.dao.StudentDao
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.network.RubricApi
import be.hogent.tile3.rubricapplication.network.asStudentDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class StudentRepository(private val studentDao: StudentDao) {

    @Inject
    lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    @WorkerThread
    fun get(id: Long): LiveData<Student> {
        return studentDao.getBy(id)
    }

    @WorkerThread
    fun getAllStudents(): LiveData<List<Student>> {
        return studentDao.getAll()
    }

    @WorkerThread
    fun getAllStudentsFromOpleidingsOnderdeel(id: Long): LiveData<List<Student>> {
        return studentDao.getAllStudentsFromOpleidingsOnderdeel(id)
    }

    suspend fun refreshStudenten(){
        Log.i("Test", "refresh called in studentRepo")
        try{
            val studenten = rubricApi.getStudenten().await()
            withContext(Dispatchers.IO){
                studentDao.insertAll(*studenten.asStudentDatabaseModel())
            }
            studenten.map {
                Log.i("Test", it.naam + "from refreshRubric in repository")
            }
        } catch (e: IOException){
            Log.i("StudentRepository", e.message)
        }
    }

    val studenten: LiveData<List<Student>> = studentDao.getAll()

}