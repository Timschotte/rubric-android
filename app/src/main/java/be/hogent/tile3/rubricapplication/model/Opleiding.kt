package be.hogent.tile3.rubricapplication.model

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json

@Entity(tableName = "opleiding_table")
data class Opleiding(

    @PrimaryKey
    @ColumnInfo(name = "opleidingId")
    @Json(name = "id")
    var opleidingId: Int = 0,

    @ColumnInfo(name = "naam")
    var naam: String
)