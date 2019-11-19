package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Student

@Dao
interface StudentDao{

    @Insert
    fun insert(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseStudent: Student)

    @Update
    fun update(student: Student)

    @Query("SELECT * from student_table WHERE studentId = :key")
    fun getBy(key: Long): LiveData<Student>

    @Query("SELECT * from student_table ORDER BY studentNaam")
    fun getAll(): LiveData<List<Student>>
}