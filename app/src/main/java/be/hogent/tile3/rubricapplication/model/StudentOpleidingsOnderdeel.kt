package be.hogent.tile3.rubricapplication.model

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Model class for [StudentOpleidingsOnderdeel]
 * @constructor Creates a [StudentOpleidingsOnderdeel]-object
 * @property studentId              ID for [Student]
 * @property opleidingsOnderdeelId  ID for [OpleidingsOnderdeel]
 */
@Entity(
    tableName = "student_opleidingsonderdeel_table",
    primaryKeys = ["studentId", "opleidingsOnderdeelId"],
    foreignKeys = [
        ForeignKey(entity = Student::class,
            parentColumns = ["studentId"],
            childColumns = ["studentId"]),
        ForeignKey(entity = OpleidingsOnderdeel::class,
            parentColumns = ["opleidingsOnderdeelId"],
            childColumns = ["opleidingsOnderdeelId"])
    ])
data class StudentOpleidingsOnderdeel(
    val studentId: Long,
    val opleidingsOnderdeelId: Long
)