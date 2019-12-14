package be.hogent.tile3.rubricapplication.model

import androidx.room.*
import java.util.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = Student::class,
        parentColumns = arrayOf("studentId"),
        childColumns = arrayOf("studentId")
    ), ForeignKey(
        entity = Rubric::class,
        parentColumns = arrayOf("rubricId"),
        childColumns = arrayOf("rubricId"),
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("studentId")]
    , tableName = "evaluatie_table"
)
data class Evaluatie(
    @PrimaryKey @ColumnInfo(name = "evaluatieId") var evaluatieId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "studentId") val studentId: Long,
    @ColumnInfo(name = "rubricId") val rubricId: Long,
    @ColumnInfo(name = "docentId") val docentId: String,
    @ColumnInfo(name = "sync") var sync: Boolean
){
    @Ignore
    var criteriumEvaluaties: List<CriteriumEvaluatie> = emptyList()
}