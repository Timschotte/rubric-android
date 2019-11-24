package be.hogent.tile3.rubricapplication.persistence

import android.util.Log
import androidx.annotation.WorkerThread
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

class StudentRepository(private val studentDao: StudentDao, private val studentOpleidingsOnderdeelDao: StudentOpleidingsOnderdeelDao) {

    @Inject
    lateinit var rubricApi: RubricApi

    init {
        App.component.inject(this)
    }

    fun get(id: Long): LiveData<Student> {
        return studentDao.getBy(id)
    }

    @WorkerThread
    fun getAllStudents(): LiveData<List<Student>> {
        return studentDao.getAll()
    }

    @WorkerThread
    fun getAllStudentsFromOpleidingsOnderdeel(id: Long): LiveData<List<Student>> {
        return studentOpleidingsOnderdeelDao.getStudentenFromOpleidingsOnderdeel(id)
    }


    suspend fun refreshStudenten(){
        Log.i("Test", "refresh called in studentRepo")
        try{
            withContext(Dispatchers.IO){
                val studenten = rubricApi.getStudenten().await()
                studentDao.insertAll(*studenten.asStudentDatabaseModel())
                studentOpleidingsOnderdeelDao.insertAll(*studenten.asStudentOpleidingsOnderdeelDatabaseModel())
                System.out.printf(studenten.asStudentOpleidingsOnderdeelDatabaseModel().toString())
                studenten.map {
                    Log.i("Test", it.naam + "from refreshRubric in repository")
                }
            }

        } catch (e: IOException){
            Log.i("StudentRepository", e.message)
        }
    }

    val studenten: LiveData<List<Student>> = studentDao.getAll()

}