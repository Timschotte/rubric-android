package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import java.io.Serializable

class RubricResource(
    @field:Json(name = "rubric") val rubric: Rubric,
    @field:Json(name = "criteriumNiveaus") val criteriumNiveaus: List<CriteriumNiveau>
) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Rubric::class.java.classLoader),
        parcel.createTypedArrayList(CriteriumNiveau)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(rubric, flags)
        parcel.writeTypedList(criteriumNiveaus)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RubricResource> {
        override fun createFromParcel(parcel: Parcel): RubricResource {
            return RubricResource(parcel)
        }

        override fun newArray(size: Int): Array<RubricResource?> {
            return arrayOfNulls(size)
        }
    }
}