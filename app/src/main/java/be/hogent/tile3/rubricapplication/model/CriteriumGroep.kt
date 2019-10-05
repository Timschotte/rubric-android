package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class CriteriumGroep(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "naam") val naam: String = "",
    @field:Json(name = "omschrijving") val omschrijving: String = "",
    @field:Json(name = "gewicht") val gewicht: String = "",
    @field:Json(name = "volgnummer") val volgnummer: String = "",
    @field:Json(name = "kleur") val kleur: String = "",
    @field:Json(name = "criteria") val criteria: List<Criterium>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Criterium)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(naam)
        parcel.writeString(omschrijving)
        parcel.writeString(gewicht)
        parcel.writeString(volgnummer)
        parcel.writeString(kleur)
        parcel.writeTypedList(criteria)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CriteriumGroep> {
        override fun createFromParcel(parcel: Parcel): CriteriumGroep {
            return CriteriumGroep(parcel)
        }

        override fun newArray(size: Int): Array<CriteriumGroep?> {
            return arrayOfNulls(size)
        }
    }
}