package be.hogent.tile3.rubricapplication.domain

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class Rubric(
    val rubricId: String = "",
    val onderwerp: String = "",
    val omschrijving: String = "",
    val datumTijdCreatie: String = "",
    val datumTijdLaatsteWijziging: String = "",
    val opleidingsOnderdeelId: OpleidingsOnderdeel
)

class Criterium(
    val criteriumId: String,
    val naam: String = "",
    val omschrijving: String = "",
    val gewicht: Double = 0.0,
    val rubric: Rubric
)

class CriteriumEvaluatie(
    id: String,
    evaluatie: Evalautie,
    criterium: Criterium,
    behaaldNiveau: CriteriumNiveau

)

class CriteriumNiveau()

class OpleidingsOnderdeel(val id: Long, val naam: String)

class CriteriumGroep(id: Long, criteria: List<Criterium>)

class Student(studentId: Long, naam: String, studentenNr: String)

class Evalautie(rubric: Rubric, student: Student)