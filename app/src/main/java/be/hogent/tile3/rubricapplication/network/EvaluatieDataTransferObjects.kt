package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Evaluatie
import com.squareup.moshi.JsonClass
import java.util.*

data class NetworkRubricEvaluatie(
    val id: Int?,
    val docentId: String,
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
fun List<NetworkRubricEvaluatie>.asDatabaseModel() : MutableList<Evaluatie> {
    return this.map { it.asDatabaseModel() }.toMutableList()
}
fun NetworkRubricEvaluatie.asDatabaseModel(): Evaluatie {
    val uuid = UUID.randomUUID().toString()
    val evaluatie = Evaluatie(
        evaluatieId = uuid,
        studentId = this.studentId,
        docentId = this.docentId,
        rubricId = this.rubricId,
        sync = true
    )
    evaluatie.criteriumEvaluaties = this.criteriumEvaluaties.map {
        CriteriumEvaluatie(
            uuid, it.criteriumId,
            it.behaaldNiveauId,
            it.niveauScore,
            it.commentaar
        )
    }
    return evaluatie;
}
fun Evaluatie.asNetworkModel(networkId : Int?): NetworkRubricEvaluatie{
    val networkCriteriumEvaluaties = mutableListOf<NetworkCriteriumEvaluatie>()
    this.criteriumEvaluaties?.forEach { critEval -> networkCriteriumEvaluaties.add(critEval.asNetworkModel())}
    return NetworkRubricEvaluatie(networkId, this.docentId, this.studentId, this.rubricId, networkCriteriumEvaluaties)
}
fun CriteriumEvaluatie.asNetworkModel(): NetworkCriteriumEvaluatie{
    return NetworkCriteriumEvaluatie(this.score, this.commentaar, this.criteriumId, this.behaaldNiveau)
}