package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity( tableName = "student_table", foreignKeys = arrayOf(ForeignKey(
    entity = OpleidingsOnderdeel::class,
    parentColumns = arrayOf("opleidingsOnderdeelId"),
    childColumns = arrayOf("opleidingsOnderdeelId"))),
    indices = arrayOf(Index("opleidingsOnderdeelId")))
data class Student(

    @PrimaryKey @ColumnInfo(name = "studentId")
    val studentId: String,

    @ColumnInfo(name = "studentNaam")
    val studentNaam: String,

    @ColumnInfo(name = "studentNr")
    val studentenNr: String,

    @ColumnInfo(name = "opleidingsOnderdeelId")
    val opleidingsOnderdeelId: Int
)