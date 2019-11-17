package be.hogent.tile3.rubricapplication.network

data class NetworkRubricEvaluatie(
    val commentaar: String,
    val docentId: Int,
    val studentId: Int,
    val rubricId: Int,
    val criteriumEvaluaties: List<NetworkCriteriumEvaluatie>
)

data class NetworkCriteriumEvaluatie(
    val id: Int?,
    val niveauScore: Int,
    val commentaar: String,
    val criteriumId: Int,
    val behaaldNiveauId: Int
)

