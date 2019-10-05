package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class CriteriumNiveau(
    @field:Json(name = "vereisten") val vereisten: String = "",
    @field:Json(name = "ondergrens") val ondergrens: String = "",
    @field:Json(name = "bovengrens") val bovengrens: String = "",
    @field:Json(name = "niveauId") val niveauId: String = "",
    @field:Json(name = "criterumId") val criteriumId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vereisten)
        parcel.writeString(ondergrens)
        parcel.writeString(bovengrens)
        parcel.writeString(niveauId)
        parcel.writeString(criteriumId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CriteriumNiveau> {
        override fun createFromParcel(parcel: Parcel): CriteriumNiveau {
            return CriteriumNiveau(parcel)
        }

        override fun newArray(size: Int): Array<CriteriumNiveau?> {
            return arrayOfNulls(size)
        }
    }
}