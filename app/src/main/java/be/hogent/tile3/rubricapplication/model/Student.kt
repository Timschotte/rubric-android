package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity( tableName = "student_table", foreignKeys = arrayOf(ForeignKey(
    entity = Opleiding::class,
    parentColumns = arrayOf("opleidingId"),
    childColumns = arrayOf("opleidingId"))),
    indices = arrayOf(Index("opleidingId")))
data class Student(

    @PrimaryKey @ColumnInfo(name = "studentId")
    val studentId: String,

    @ColumnInfo(name = "studentNaam")
    val studentNaam: String,

    @ColumnInfo(name = "studentNr")
    val studentenNr: String,

    @ColumnInfo(name = "opleidingId")
    val opleidingId: Int
)