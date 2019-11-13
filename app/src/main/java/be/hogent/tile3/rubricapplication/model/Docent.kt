package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity(
    tableName = "docent_table",
    foreignKeys = arrayOf(ForeignKey(
        entity = OpleidingsOnderdeel::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("id")
    )),
    indices = arrayOf(Index("id"))
)
data class Docent(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="docentId")
    val docentId: Int = 0,

    @ColumnInfo(name = "naam")
    val naam: String,

    @ColumnInfo(name = "id")
    val opleidingId: Int
)