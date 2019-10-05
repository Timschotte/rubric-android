package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class Niveau(
    @field:Json(name = "id") val id: String = "",
    @field:Json(name = "naam") val naam: String = "",
    @field:Json(name = "volgnummer") val volgnummer: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(naam)
        parcel.writeString(volgnummer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Niveau> {
        override fun createFromParcel(parcel: Parcel): Niveau {
            return Niveau(parcel)
        }

        override fun newArray(size: Int): Array<Niveau?> {
            return arrayOfNulls(size)
        }
    }
}