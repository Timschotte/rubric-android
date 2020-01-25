package be.hogent.tile3.rubricapplication.network

import be.hogent.tile3.rubricapplication.model.OpleidingsOnderdeel
import android.util.Log
import be.hogent.tile3.rubricapplication.model.Criterium
import be.hogent.tile3.rubricapplication.model.Niveau
import be.hogent.tile3.rubricapplication.model.Rubric
import be.hogent.tile3.rubricapplication.model.Student
import be.hogent.tile3.rubricapplication.model.StudentOpleidingsOnderdeel
import com.squareup.moshi.JsonClass
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf

@JsonClass(generateAdapter = false)
data class NetworkRubric(
    val id: Long,
    val onderwerp: String,
    val omschrijving: String,
    val criteriumGroepen: List<NetworkCriteriumGroep>,
    val niveauSchaal: NetworkNiveauSchaal,
    val datumTijdCreatie: String,
    val datumTijdLaatsteWijziging: String,
    //val opleidingsOnderdeel: Long
    val opleidingsOnderdeel: NetworkOpleidingsOnderdeel
)

@JsonClass(generateAdapter = false)
data class NetworkCriteriumGroep(
    val id: Long,
    val criteria: List<NetworkCriterium>
)

@JsonClass(generateAdapter = false)
data class NetworkNiveauSchaal(
    val id: Long,
    val niveaus: List<NetworkNiveau>
)

@JsonClass(generateAdapter = false)
data class NetworkOpleidingsOnderdeel(
    val id: Long,
    val naam: String,
    val docenten: List<String>,
    val studenten: List<Long>,
    val rubrics: List<Long>
)

@JsonClass(generateAdapter = false)
data class NetworkCriterium(
    val id: Long,
    val naam: String,
    val omschrijving: String,
    val gewicht: Double,
    val volgnummer: Int,
    val criteriumNiveaus: List<NetworkCriteriumNiveau>
)

@JsonClass(generateAdapter = false)
data class NetworkCriteriumNiveau(
    val id: Long,
    val omschrijving: String,
    val ondergrens: Int,
    val bovengrens: Int,
    val niveau: NetworkNiveau
)

@JsonClass(generateAdapter = false)
data class NetworkNiveau(
    val id: Int,
    val naam: String,
    val volgnummer: Int
)

@JsonClass(generateAdapter = false)
data class NetworkStudent(
    val id: Long,
    val naam: String,
    val achternaam: String?,
    val voornaam: String?,
    val geboorteDatum: String?,
    val studentNummer: String,
    val opleidingsOnderdelen: List<Long>
)

/**
 * Transformeert opgehaalde lijst van netwerk naar een lijst van Rubric (in domain package)
 */
fun NetworkRubric.asDatabaseModel(): Rubric{
    return Rubric(
        this.id,
        this.onderwerp,
        this.omschrijving,
        this.datumTijdCreatie,
        this.datumTijdLaatsteWijziging,
        this.opleidingsOnderdeel.id
    )
}

fun List<NetworkOpleidingsOnderdeel>.asOpleidingsOnderdeelDatabaseModel(): Array<OpleidingsOnderdeel> {
    return map {
        OpleidingsOnderdeel(
            it.id,
            it.naam
        )
    }.toTypedArray()
}

fun List<NetworkStudent>.asStudentDatabaseModel(): Array<Student> {
    return map {
        Student(
            it.id,
            it.naam,
            it.achternaam,
            it.voornaam,
            it.geboorteDatum,
            it.studentNummer
        )
    }.toTypedArray()
}

fun List<NetworkStudent>.asStudentOpleidingsOnderdeelDatabaseModel(): Array<StudentOpleidingsOnderdeel>{
    val list = ArrayList<StudentOpleidingsOnderdeel>()
    for (networkStudent in this) {
        networkStudent.opleidingsOnderdelen.map { oo ->
            list.add(StudentOpleidingsOnderdeel(networkStudent.id, oo))
        }
    }
    return list.toTypedArray()
}


fun NetworkCriterium.asDatabaseModel(rubricId: Long, criteriumGroepId: Long): Criterium{
    return Criterium(
        this.id,
        this.naam,
        this.omschrijving,
        this.gewicht,
        criteriumGroepId,
        rubricId
    )
}

fun NetworkCriteriumNiveau.asDatabaseModel(rubricId: Long, criteriumGroepId: Long, criteriumId: Long): Niveau{
    return Niveau(
        this.id,
        this.niveau.naam,
        this.omschrijving,
        this.ondergrens,
        this.bovengrens,
        this.niveau.volgnummer,
        rubricId,
        criteriumGroepId,
        criteriumId
    )
}