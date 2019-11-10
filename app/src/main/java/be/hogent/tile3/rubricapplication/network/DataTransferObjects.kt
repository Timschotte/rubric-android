package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.Rubric
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class NetworkRubric(
    val rubricId: Long,
    val onderwerp: String,
    val omschrijving: String,
    val datumTijdCreatie: String,
    val datumLaatsteWijziging: String
)

/**
 * Transformeert opgehaalde lijst van netwerk naar een lijst van Rubric (in domain package)
 */
fun List<NetworkRubric>.asDatabaseModel(): List<Rubric> {
    return map {
        Rubric(
            it.rubricId.toString(),
            it.onderwerp,
            it.omschrijving,
            it.datumTijdCreatie,
            it.datumLaatsteWijziging
        )
    }
}