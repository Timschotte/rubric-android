package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Niveauschaal(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "naam") val naam: String = "",
    @field:Json(name = "centraalNiveau") val centraalNiveau: Niveau,
    @field:Json(name = "niveaus") val niveaus: List<Niveau>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Niveau::class.java.classLoader),
        parcel.createTypedArrayList(Niveau)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(naam)
        parcel.writeParcelable(centraalNiveau, flags)
        parcel.writeTypedList(niveaus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Niveauschaal> {
        override fun createFromParcel(parcel: Parcel): Niveauschaal {
            return Niveauschaal(parcel)
        }

        override fun newArray(size: Int): Array<Niveauschaal?> {
            return arrayOfNulls(size)
        }
    }
}