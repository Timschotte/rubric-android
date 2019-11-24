package be.hogent.tile3.rubricapplication.model

import androidx.room.*
import java.util.*

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = Student::class,
        parentColumns = arrayOf("studentId"),
        childColumns = arrayOf("studentId")
    ),
    ForeignKey(
        entity = Rubric::class,
        parentColumns = arrayOf("rubricId"),
        childColumns = arrayOf("rubricId"))
), tableName = "evaluatie_table"
)
data class Evaluatie(
    @PrimaryKey @ColumnInfo(name = "evaluatieId") var evaluatieId: String,
    @ColumnInfo(name = "studentId") val studentId: Long,
    @ColumnInfo(name = "rubricId") val rubricId: String
    // TODO: toevoegen docent en data (aanmaak, wijziging)
    // TODO: lijst criteriumevaluaties inzetten
)