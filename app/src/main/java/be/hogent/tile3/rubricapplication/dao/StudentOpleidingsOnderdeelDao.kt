package be.hogent.tile3.rubricapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.model.StudentOpleidingsOnderdeel

@Dao
interface StudentOpleidingsOnderdeelDao {
    @Query("SELECT * FROM student_table INNER JOIN student_opleidingsonderdeel_table ON student_table.studentId = student_opleidingsonderdeel_table.studentId WHERE student_opleidingsonderdeel_table.opleidingsOnderdeelId = :opleidingsOnderdeelId")
    fun getStudentenFromOpleidingsOnderdeel(opleidingsOnderdeelId: Long): LiveData<List<Student>>

    @Insert
    fun insert(studentOpleidingsOnderdeel: StudentOpleidingsOnderdeel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg databaseStudentOpleidingsOnderdeel: StudentOpleidingsOnderdeel)

}