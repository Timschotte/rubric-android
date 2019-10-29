package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class CriteriumData(
    @field:Json(name = "id") val id : Int,
    @field:Json(name = "naam") val naam : String,
    @field:Json(name = "omschrijving") val omschrijving : String,
    @field:Json(name = "gewicht") val gewicht : Int,
    @field:Json(name = "volgnummer") val volgnummer : Int,
    @field:Json(name = "criteriumNiveaus") val criteriumNiveaus : List<CriteriumNiveauData>
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.createTypedArrayList(CriteriumNiveauData)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(naam)
        parcel.writeString(omschrijving)
        parcel.writeInt(gewicht)
        parcel.writeInt(volgnummer)
        parcel.writeTypedList(criteriumNiveaus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CriteriumData> {
        override fun createFromParcel(parcel: Parcel): CriteriumData {
            return CriteriumData(parcel)
        }

        override fun newArray(size: Int): Array<CriteriumData?> {
            return arrayOfNulls(size)
        }
    }
}