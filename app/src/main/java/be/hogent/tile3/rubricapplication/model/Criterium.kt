package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Rubric::class,
            parentColumns = arrayOf("rubricId"),
            childColumns = arrayOf("rubricId"),
            onDelete = ForeignKey.CASCADE)),
    indices = arrayOf(Index(value = ["criteriumId"])),
    tableName = "criterium_table"
)
data class Criterium(
    // let op: geen criteriumgroepid
    @PrimaryKey @ColumnInfo(name = "criteriumId") val criteriumId: String,
    @ColumnInfo(name = "naam") val naam: String = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String = "",
    @ColumnInfo(name = "gewicht") val gewicht: Double = 0.0,
    @ColumnInfo(name = "criteriumGroepId") val criteriumGroepId: String,
    @ColumnInfo(name = "rubricId") val rubricId: String
) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(criteriumId)
        parcel.writeString(naam)
        parcel.writeString(omschrijving)
        parcel.writeDouble(gewicht)
        parcel.writeString(criteriumGroepId)
        parcel.writeString(rubricId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Criterium> {
        override fun createFromParcel(parcel: Parcel): Criterium {
            return Criterium(parcel)
        }

        override fun newArray(size: Int): Array<Criterium?> {
            return arrayOfNulls(size)
        }
    }
}