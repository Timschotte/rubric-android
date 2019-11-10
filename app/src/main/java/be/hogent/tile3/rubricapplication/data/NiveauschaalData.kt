package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class NiveauschaalData(
    @field:Json(name = "rubricId") val id : Int,
    @field:Json(name = "niveaus") val niveaus : List<NiveauData>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(NiveauData)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeTypedList(niveaus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NiveauschaalData> {
        override fun createFromParcel(parcel: Parcel): NiveauschaalData {
            return NiveauschaalData(parcel)
        }

        override fun newArray(size: Int): Array<NiveauschaalData?> {
            return arrayOfNulls(size)
        }
    }
}