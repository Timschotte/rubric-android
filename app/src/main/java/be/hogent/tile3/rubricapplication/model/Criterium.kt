package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Criterium(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "naam") val naam: String = "",
    @field:Json(name = "omschrijving") val omschrijving: String = "",
    @field:Json(name = "gewicht") val gewicht: String = "",
    @field:Json(name = "volgnummer") val kleur: String = "",
    @field:Json(name = "niveauschaal") val niveauschaal: Niveauschaal
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Niveauschaal::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(naam)
        parcel.writeString(omschrijving)
        parcel.writeString(gewicht)
        parcel.writeString(kleur)
        parcel.writeParcelable(niveauschaal, flags)
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