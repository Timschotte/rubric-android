package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity( tableName = "student_table")
data class Student(

    @PrimaryKey @ColumnInfo(name = "studentId")
    val studentId: Long,

    @ColumnInfo(name = "studentNaam")
    val studentNaam: String,

    @ColumnInfo(name = "studentNr")
    val studentenNr: String
)