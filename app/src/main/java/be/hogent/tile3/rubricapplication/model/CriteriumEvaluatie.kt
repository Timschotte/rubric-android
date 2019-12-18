package be.hogent.tile3.rubricapplication.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import java.util.*
/**
 * Model class for [CriteriumEvaluatie]
 * @constructor Creates a [CriteriumEvaluatie] class
 * @property criteriumEvaluatieId   ID for [CriteriumEvaluatie]
 * @property evaluatieId            ID ([Evaluatie]) for defining the [Evaluatie] the [CriteriumEvaluatie] belongs to
 * @property criteriumId            ID ([Criterium]) for defining the [Criterium] the [CriteriumEvaluatie] belongs to
 * @property behaaldNiveau          ID ([Niveau]) for defining the [Niveau] was reached for the [CriteriumEvaluatie]
 * @property score                  Score that was reached for the [CriteriumEvaluatie]
 * @property commentaar             Comment that was attached to the [CriteriumEvaluatie]
 */
@Entity(foreignKeys = [ForeignKey(
    entity = Criterium::class,
    parentColumns = arrayOf("criteriumId"),
    childColumns = arrayOf("criteriumId"))
    , ForeignKey(entity = Evaluatie::class,
    parentColumns = arrayOf("evaluatieId"),
    childColumns = arrayOf("evaluatieId"), onDelete = CASCADE), ForeignKey(
    entity = Niveau::class,
    parentColumns = arrayOf("niveauId"),
    childColumns = arrayOf("behaaldNiveau")
)],indices = [Index(value = ["criteriumId"]), Index(value= ["behaaldNiveau"])],
    tableName = "criterium_evaluatie_table")
data class CriteriumEvaluatie(
    @Transient @PrimaryKey @ColumnInfo(name = "criteriumEvaluatieId") val criteriumEvaluatieId: String,
    @Transient @ColumnInfo(name = "evaluatieId") var evaluatieId: String,
    @ColumnInfo(name = "criteriumId") val criteriumId: Long,
    @ColumnInfo(name = "behaaldNiveau") var behaaldNiveau: Long,
    @ColumnInfo(name = "niveauScore") var score: Int,
    @ColumnInfo(name = "commentaar") var commentaar: String
){
    @Ignore
    constructor(evaluatieId: String, criteriumId: Long, behaaldNiveau: Long, score: Int, commentaar: String):
            this(UUID.randomUUID().toString(), evaluatieId, criteriumId, behaaldNiveau, score, commentaar)
}