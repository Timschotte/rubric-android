package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.model.StudentOpleidingsOnderdeel
/**
 * Data Access Object for [StudentOpleidingsOnderdeel] for Room database operations
 * @see Dao
 */
@Dao
interface StudentOpleidingsOnderdeelDao {
    /**
     * Retrieves all [Student] from a given OpleidingsOnderdeel
     * @param opleidingsOnderdeelId [Long]
     * @return [LiveData] [List] of [Student]
     */
    @Query("SELECT * FROM student_table INNER JOIN student_opleidingsonderdeel_table ON student_table.studentId = student_opleidingsonderdeel_table.studentId WHERE student_opleidingsonderdeel_table.opleidingsOnderdeelId = :opleidingsOnderdeelId")
    fun getStudentenFromOpleidingsOnderdeel(opleidingsOnderdeelId: Long): LiveData<List<Student>>
    /**
     * Inserts a [StudentOpleidingsOnderdeel] in Room database.
     * @param studentOpleidingsOnderdeel [StudentOpleidingsOnderdeel]
     */
    @Insert
    fun insert(studentOpleidingsOnderdeel: StudentOpleidingsOnderdeel)
    /**
     * Inserts a list of [StudentOpleidingsOnderdeel] in Room database. Exisiting [StudentOpleidingsOnderdeel] will be replaced.
     * @see OnConflictStrategy.REPLACE
     * @param databaseStudentOpleidingsOnderdeel [StudentOpleidingsOnderdeel]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseStudentOpleidingsOnderdeel: StudentOpleidingsOnderdeel)

}