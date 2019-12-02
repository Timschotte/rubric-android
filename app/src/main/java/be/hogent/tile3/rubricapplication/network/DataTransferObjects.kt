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
    val docenten: List<Long>,
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
data class NetworkDocent(
    val id: Long,
    val naam: String,
    val opleidingsOnderdeel: NetworkOpleidingsOnderdeel
)

@JsonClass(generateAdapter = false)
data class NetworkStudent(
    val id: Long,
    val naam: String,
    val achternaam: String?,
    val voornaam: String?,
    val geboortedatum: String?,
    val studentNummer: String,
    val opleidingsOnderdelen: List<Long>
)

/**
 * Transformeert opgehaalde lijst van netwerk naar een lijst van Rubric (in domain package)
 */
fun NetworkRubric.asDatabaseModel(): Rubric{
    return Rubric(
        this.id.toString(),
        this.onderwerp,
        this.omschrijving,
        this.datumTijdCreatie,
        this.datumTijdLaatsteWijziging,
        this.opleidingsOnderdeel.id
    )
}

fun List<NetworkRubric>.asDatabaseModel(): List<Rubric> {
    return map {
        Rubric(
            it.id.toString(),
            it.onderwerp,
            it.omschrijving,
            it.datumTijdCreatie,
            it.datumTijdLaatsteWijziging,
            it.opleidingsOnderdeel.id
        )
    }
}

fun List<NetworkRubric>.asDatabaseModelArray(): Array<Rubric> {
    return map {
        Rubric(
            it.id.toString(),
            it.onderwerp,
            it.omschrijving,
            it.datumTijdCreatie,
            it.datumTijdLaatsteWijziging,
            it.opleidingsOnderdeel.id
        )
    }.toTypedArray()
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
            it.geboortedatum,
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


fun NetworkCriterium.asDatabaseModel(rubricId: String, criteriumGroepId: String): Criterium{
    Log.i("DTO", "About to return Criterium for rubric " + rubricId + ", criteriumGroepId: " + criteriumGroepId + " and criteriumId: " + this.id.toString())
    return Criterium(
        this.id.toString(),
        this.naam,
        this.omschrijving,
        this.gewicht,
        criteriumGroepId,
        rubricId
    )
}

fun List<NetworkCriterium>.asDatabaseModelArray(rubricId: String, criteriumGroepId: String): Array<Criterium>{
    return map{
        Criterium(
            it.id.toString(),
            it.naam,
            it.omschrijving,
            it.gewicht,
            criteriumGroepId,
            rubricId
        )
    }.toTypedArray()
}

fun NetworkCriteriumNiveau.asDatabaseModel(rubricId: String, criteriumGroepId: String, criteriumId: String): Niveau{
    Log.i("DTO", "About to return Niveau for rubric " + rubricId + ", criteriumGroep " + criteriumGroepId + ", criterium" + criteriumId + ", niveauId: " + this.id.toString())
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

fun List<NetworkCriteriumNiveau>.asDatabaseModelArray(rubricId: String, criteriumGroepId: String, criteriumId: String): Array<Niveau>{
    return map{
        Niveau(
            it.id,
            it.niveau.naam,
            it.omschrijving,
            it.ondergrens,
            it.bovengrens,
            it.niveau.volgnummer,
            rubricId,
            criteriumGroepId,
            criteriumId
        )
    }.toTypedArray()
}