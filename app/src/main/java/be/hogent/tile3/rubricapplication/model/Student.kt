package be.hogent.tile3.rubricapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity( tableName = "student_table", foreignKeys = arrayOf(ForeignKey(
    entity = Opleiding::class,
    parentColumns = arrayOf("student_id"),
    childColumns = arrayOf("opleiding_id")
)))
data class Student(

    @PrimaryKey @ColumnInfo(name = "student_id")
    val studentId: String,

    @ColumnInfo(name = "studentNaam")
    val studentNaam: String,

    @ColumnInfo(name = "studentNr")
    val studentenNr: String,

    @ColumnInfo(name = "opleiding_id")
    val opleidingId: Int
)