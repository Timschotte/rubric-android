package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Evaluatie
import java.util.*

data class NetworkRubricEvaluatie(
    val id: Int,
    val docentId: Long,
    val studentId: Long,
    val rubricId: Long,
    val criteriumEvaluaties: List<NetworkCriteriumEvaluatie>
)
@JsonClass(generateAdapter = false)
data class NetworkCriteriumEvaluatie(
    val niveauScore: Int,
    val commentaar: String,
    val criteriumId: Long,
    val behaaldNiveauId: Long
)

fun NetworkRubricEvaluatie.asDatabaseModel(): Pair<Evaluatie,List<CriteriumEvaluatie>> {
    val uuid = UUID.randomUUID().toString()
    return Pair(Evaluatie(evaluatieId = uuid,
        studentId = this.studentId,
        docentId = this.docentId,
        rubricId = this.rubricId),
        this.criteriumEvaluaties.map {
            CriteriumEvaluatie(uuid, it.criteriumId,
                it.behaaldNiveauId,
                it.niveauScore,
                it.commentaar)
        })


}