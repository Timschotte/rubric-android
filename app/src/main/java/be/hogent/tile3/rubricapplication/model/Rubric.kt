package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName = "rubric_table",
    foreignKeys = [ForeignKey(
        entity = OpleidingsOnderdeel::class,
        parentColumns = arrayOf("opleidingsOnderdeelId"),
        childColumns = arrayOf("opleidingsOnderdeelId"),
        onDelete = ForeignKey.SET_NULL)],
    indices = [Index("opleidingsOnderdeelId")]
)
data class Rubric(
    @PrimaryKey @ColumnInfo(name = "rubricId") val rubricId: Long = 0,
    @ColumnInfo(name = "onderwerp") val onderwerp: String? = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String? = "",
    @ColumnInfo(name = "datumTijdCreatie") val datumTijdCreatie: String? = "",
    @ColumnInfo(name = "datumTijdLaatsteWijziging") val datumTijdLaatsteWijziging: String? = "",
    @ColumnInfo(name = "opleidingsOnderdeelId") val opleidingsOnderdeelId: Long?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(rubricId)
        parcel.writeString(onderwerp)
        parcel.writeString(omschrijving)
        parcel.writeString(datumTijdCreatie)
        parcel.writeString(datumTijdLaatsteWijziging)
        parcel.writeLong(opleidingsOnderdeelId!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rubric> {
        override fun createFromParcel(parcel: Parcel): Rubric {
            return Rubric(parcel)
        }

        override fun newArray(size: Int): Array<Rubric?> {
            return arrayOfNulls(size)
        }
    }
}