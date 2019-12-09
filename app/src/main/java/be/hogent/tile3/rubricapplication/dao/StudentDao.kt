package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.hogent.tile3.rubricapplication.model.Student
/**
 * Data Access Object for [Student] for Room database operations
 * @see Dao
 */
@Dao
interface StudentDao{
    /**
     * Inserts a [Student] in [Room] database. Exisiting [Student] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param student [Student]
     */
    @Insert
    fun insert(student: Student)
    /**
     * Inserts a list of [Student] in [Room] database. Exisiting [Student] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param databaseStudent [Student]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseStudent: Student)
    /**
     * Updates a [Student] in [Room] database
     * @param student [Student]
     */
    @Update
    fun update(student: Student)
    /**
     * Retrieves [Student] by id
     * @param studentId [Long]
     * @return [LiveData] [Student]
     */
    @Query("SELECT * from student_table WHERE studentId = :studentId")
    fun getBy(studentId: Long): LiveData<Student>
    /**
     * Retrieves all [Student] from [Room] database order by studentNaam
     * @return [LiveData] list of [Student]
     */
    @Query("SELECT * from student_table ORDER BY studentNaam")
    fun getAll(): LiveData<List<Student>>
}