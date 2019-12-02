package be.hogent.tile3.rubricapplication.model

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Student::class,
            parentColumns = arrayOf("studentId"),
            childColumns = arrayOf("studentId")
        ),
        ForeignKey(
            entity = Rubric::class,
            parentColumns = arrayOf("rubricId"),
            childColumns = arrayOf("rubricId"),
            onDelete = ForeignKey.CASCADE
        )
        /*,
        ForeignKey(
            entity = Docent::class,
            parentColumns = arrayOf("docentId"),
            childColumns = arrayOf("docentId")
        )*/
), indices = [Index("studentId")]
    , tableName = "evaluatie_table"
)
data class Evaluatie(
    @PrimaryKey @ColumnInfo(name = "evaluatieId") var evaluatieId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "studentId") val studentId: Long,
    @ColumnInfo(name = "rubricId") val rubricId: Long,
    @ColumnInfo(name = "docentId") val docentId: Long
){
    @Ignore
    var criteriumEvaluaties: List<CriteriumEvaluatie>? = null
}