package be.hogent.tile3.rubricapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "docent_table", foreignKeys = arrayOf(ForeignKey(
    entity = Opleiding::class,
    parentColumns = arrayOf("docent_id"),
    childColumns = arrayOf("opleiding_id")

)))
data class Docent(

    @PrimaryKey(autoGenerate = true)
    val docentId: Int = 0,

    @ColumnInfo(name = "naam")
    val naam: String,

    @ColumnInfo(name = "opleiding_id")
    val opleidingId: Int
)