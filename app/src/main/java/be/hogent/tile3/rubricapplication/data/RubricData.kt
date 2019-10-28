package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class RubricData(
    @field:Json(name = "id") val id : Int,
    @field:Json(name = "onderwerp") val onderwerp : String,
    @field:Json(name = "omschrijving") val omschrijving : String,
    @field:Json(name = "criteriumGroepen") val criteriumGroepen : List<CriteriumGroepData>,
    @field:Json(name = "niveauSchaal") val niveauSchaal : NiveauschaalData,
    @field:Json(name = "datumTijdCreatie") val datumTijdCreatie : String,
    @field:Json(name = "datumTijdLaatsteWijziging") val datumTijdLaatsteWijziging : String

    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(CriteriumGroepData),
        parcel.readParcelable(NiveauschaalData::class.java.classLoader),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(onderwerp)
        parcel.writeString(omschrijving)
        parcel.writeTypedList(criteriumGroepen)
        parcel.writeParcelable(niveauSchaal, flags)
        parcel.writeString(datumTijdCreatie)
        parcel.writeString(datumTijdLaatsteWijziging)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RubricData> {
        override fun createFromParcel(parcel: Parcel): RubricData {
            return RubricData(parcel)
        }

        override fun newArray(size: Int): Array<RubricData?> {
            return arrayOfNulls(size)
        }
    }
}