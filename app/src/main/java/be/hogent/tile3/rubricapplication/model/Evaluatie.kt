package be.hogent.tile3.rubricapplication.model

import androidx.room.*
import java.util.*

/**
 * Model class for [Evaluatie]
 * @constructor Creates a [Evaluatie] class
 * @property evaluatieId            ID for [Evaluatie]
 * @property studentId              ID ([Student]) that the [Evaluatie] belongs to
 * @property rubricId               ID ([Rubric]) that the [Evaluatie] belongs to
 * @property docentId               ID ([Docent]) that the [Evaluatie] belongs to
 * @property sync                   Indicating the [SyncStatus] for the [Evaluatie]
 * @property criteriumEvaluaties    List of [CriteriumEvaluatie] that belongs to the [Evaluatie]
 */
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
    @ColumnInfo(name = "docentId") val docentId: Long,
    @ColumnInfo(name = "sync") var sync: Boolean
){
    @Ignore
    var criteriumEvaluaties: List<CriteriumEvaluatie> = emptyList()
}