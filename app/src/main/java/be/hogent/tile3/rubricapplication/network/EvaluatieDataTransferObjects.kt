package be.hogent.tile3.rubricapplication.network

import android.util.Log
import be.hogent.tile3.rubricapplication.model.CriteriumEvaluatie
import be.hogent.tile3.rubricapplication.model.Evaluatie
import com.google.gson.Gson
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = false)
data class NetworkRubricEvaluatie(
    var id: String?,
    val commentaar: String,
    val docentId: Int?,
    val docent: Int?,
    val studentId: Long?,
    val student: Long?,
    val rubricId: Long?,
    val rubric: Long?,
    val criteriumEvaluaties: List<NetworkCriteriumEvaluatie>
)
@JsonClass(generateAdapter = false)
data class NetworkCriteriumEvaluatie(
    var id: String?,
    val niveauScore: Int?,
    val commentaar: String?,
    val criteriumId: Long?,
    val criterium: Long?,
    val behaaldNiveauId: Long?
)

fun Evaluatie.asNetworkModel(): NetworkRubricEvaluatie{
    var networkCriteriumEvaluaties = mutableListOf<NetworkCriteriumEvaluatie>()
    this.criteriumEvaluaties?.forEach { critEval -> networkCriteriumEvaluaties.add(critEval.asNetworkModel())}
    return NetworkRubricEvaluatie(this.evaluatieId ,"", 1, null, this.studentId, null, this.rubricId.toLong(),null,  networkCriteriumEvaluaties)
}
fun CriteriumEvaluatie.asNetworkModel(): NetworkCriteriumEvaluatie{
    return NetworkCriteriumEvaluatie(this.criteriumEvaluatieId, this.score, this.commentaar, this.criteriumId.toLong(), null, this.behaaldNiveau)
}
fun NetworkRubricEvaluatie.logJson(){
    val gson = Gson()
    val str = gson.toJson(this)
    Log.i("TestN", "GSON object:"+str)
}