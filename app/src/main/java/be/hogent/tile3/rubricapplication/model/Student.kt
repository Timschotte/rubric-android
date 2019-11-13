package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity( tableName = "student_table", foreignKeys = arrayOf(ForeignKey(
    entity = OpleidingsOnderdeel::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("id"))),
    indices = arrayOf(Index("id")))
data class Student(

    @PrimaryKey @ColumnInfo(name = "studentId")
    val studentId: String,

    @ColumnInfo(name = "studentNaam")
    val studentNaam: String,

    @ColumnInfo(name = "studentNr")
    val studentenNr: String,

    @ColumnInfo(name = "id")
    val opleidingId: Int
)