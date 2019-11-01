package be.hogent.tile3.rubricapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "student_table")
data class Student(
    @PrimaryKey @ColumnInfo(name = "studentId") val studentId: String,
    @ColumnInfo(name = "studentNaam") val studentNaam: String,
    @ColumnInfo(name = "studentNr") val studentenNr: String
)