package be.hogent.tile3.rubricapplication.model

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json

@Entity(tableName = "opleidingsOnderdeel_table")
data class OpleidingsOnderdeel(

    @PrimaryKey
    @ColumnInfo(name = "id")
    @Json(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "naam")
    var naam: String
)