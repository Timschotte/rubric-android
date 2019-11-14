package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity(foreignKeys = arrayOf(
    ForeignKey(
        entity = Criterium::class,
        parentColumns = arrayOf("criteriumId"),
        childColumns = arrayOf("criteriumId")),
    ForeignKey(
        entity = Evaluatie::class,
        parentColumns = arrayOf("evaluatieId"),
        childColumns = arrayOf("evaluatieId")),
    ForeignKey(
        entity = Niveau::class,
        parentColumns = arrayOf("niveauId"),
        childColumns = arrayOf("behaaldNiveau")
    )
),
    indices = arrayOf(Index(value = ["criteriumId"]),
        Index(value= ["behaaldNiveau"])),
    tableName = "criterium_evaluatie_table")
data class CriteriumEvaluatie(
    @PrimaryKey @ColumnInfo(name = "criteriumEvaluatieId") val criteriumEvaluatieId: String,
    // TODO: weghalen evaluatieId
    @ColumnInfo(name = "evaluatieId") val evaluatieId: String,
    @ColumnInfo(name = "criteriumId") val criteriumId: String,
    // Merk op: we verwachten hier een waarde voor behaaldNiveau. By default is dit het centrale
    // niveau, zodat dit in de GUI bij start goed weergegeven wordt. Dit is het criteriumniveau met
    // volgnummer 0.
    @ColumnInfo(name = "behaaldNiveau") var behaaldNiveau: String,
    @ColumnInfo(name = "score") var score: Int?,
    @ColumnInfo(name = "commentaar") var commentaar: String?
)