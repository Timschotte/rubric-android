package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class CriteriumNiveauData (
    @field:Json(name = "rubricId") val id : Int,
    @field:Json(name = "omschrijving") val omschrijving : String,
    @field:Json(name = "ondergrens") val ondergrens : Int,
    @field:Json(name = "bovengrens") val bovengrens : Int,
    @field:Json(name = "niveau") val niveau : NiveauData
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(NiveauData::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(omschrijving)
        parcel.writeInt(ondergrens)
        parcel.writeInt(bovengrens)
        parcel.writeParcelable(niveau, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CriteriumNiveauData> {
        override fun createFromParcel(parcel: Parcel): CriteriumNiveauData {
            return CriteriumNiveauData(parcel)
        }

        override fun newArray(size: Int): Array<CriteriumNiveauData?> {
            return arrayOfNulls(size)
        }
    }
}