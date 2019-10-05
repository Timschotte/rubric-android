package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

data class Rubric(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "onderwerp") val onderwerp: String = "",
    @field:Json(name = "omschrijving") val omschrijving: String = "",
    @field:Json(name = "datumTijdCreatie") val datumTijdCreatie: String = "",
    @field:Json(name = "datumTijdLaatsteWijziging") val datumTijdLaatsteWijziging: String = "",
    @field:Json(name = "criteriumGroepen") val criteriumGroepen: List<CriteriumGroep>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(CriteriumGroep)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(onderwerp)
        parcel.writeString(omschrijving)
        parcel.writeString(datumTijdCreatie)
        parcel.writeString(datumTijdLaatsteWijziging)
        parcel.writeTypedList(criteriumGroepen)
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