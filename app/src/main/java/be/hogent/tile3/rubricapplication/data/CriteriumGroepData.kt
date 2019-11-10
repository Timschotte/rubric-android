package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class CriteriumGroepData(
    @field:Json(name ="rubricId") val id : Int,
    @field:Json(name ="criteria") val criteria : List<CriteriumData>
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(CriteriumData)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeTypedList(criteria)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CriteriumGroepData> {
        override fun createFromParcel(parcel: Parcel): CriteriumGroepData {
            return CriteriumGroepData(parcel)
        }

        override fun newArray(size: Int): Array<CriteriumGroepData?> {
            return arrayOfNulls(size)
        }
    }
}