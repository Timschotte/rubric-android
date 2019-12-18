package be.hogent.tile3.rubricapplication.model

import androidx.room.*
import com.squareup.moshi.Json

/**
 * Model class for [OpleidingsOnderdeel]
 * @constructor Creates an [OpleidingsOnderdeel] object
 * @property id     ID for [OpleidingsOnderdeel]
 * @property naam   Name for [OpleidingsOnderdeel]
 */
@Entity(tableName = "opleidingsOnderdeel_table")
data class OpleidingsOnderdeel(

    @PrimaryKey
    @ColumnInfo(name = "opleidingsOnderdeelId")
    @Json(name = "opleidingsOnderdeelId")
    var id: Long = 0,

    @ColumnInfo(name = "naam")
    var naam: String
)