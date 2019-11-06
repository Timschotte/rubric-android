package be.hogent.tile3.rubricapplication.model

import androidx.room.*

@Entity(
    tableName = "docent_table",
    foreignKeys = arrayOf(ForeignKey(
        entity = Opleiding::class,
        parentColumns = arrayOf("opleidingId"),
        childColumns = arrayOf("opleidingId")
    )),
    indices = arrayOf(Index("opleidingId"))
)
data class Docent(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="docentId")
    val docentId: Int = 0,

    @ColumnInfo(name = "naam")
    val naam: String,

    @ColumnInfo(name = "opleidingId")
    val opleidingId: Int
)