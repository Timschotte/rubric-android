package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class NiveauData(
    @field:Json(name = "rubricId") val id : Int,
    @field:Json(name = "naam") val naam : String,
    @field:Json(name = "volgnummer") val volgnummer : Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(naam)
        parcel.writeInt(volgnummer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NiveauData> {
        override fun createFromParcel(parcel: Parcel): NiveauData {
            return NiveauData(parcel)
        }

        override fun newArray(size: Int): Array<NiveauData?> {
            return arrayOfNulls(size)
        }
    }
}