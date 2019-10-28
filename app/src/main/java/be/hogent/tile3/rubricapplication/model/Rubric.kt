package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(tableName = "rubric_table")
data class Rubric(
    @PrimaryKey @ColumnInfo(name = "rubricId") val rubricId: String = "",
    @ColumnInfo(name = "onderwerp") val onderwerp: String = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String = "",
    @ColumnInfo(name = "datumTijdCreatie") val datumTijdCreatie: String = "",
    @ColumnInfo(name = "datumTijdLaatsteWijziging") val datumTijdLaatsteWijziging: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rubricId)
        parcel.writeString(onderwerp)
        parcel.writeString(omschrijving)
        parcel.writeString(datumTijdCreatie)
        parcel.writeString(datumTijdLaatsteWijziging)
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