package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity(
    tableName = "docent_table",
    foreignKeys = arrayOf(ForeignKey(
        entity = OpleidingsOnderdeel::class,
        parentColumns = arrayOf("opleidingsOnderdeelId"),
        childColumns = arrayOf("opleidingsOnderdeelId")
    )),
    indices = arrayOf(Index("opleidingsOnderdeelId"))
)
data class Docent(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="docentId")
    val docentId: Int = 0,

    @ColumnInfo(name = "naam")
    val naam: String,

    @ColumnInfo(name = "opleidingsOnderdeelId")
    val opleidingId: Int
)