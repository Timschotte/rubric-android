package be.hogent.tile3.rubricapplication.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class RubricsResource(
    @field:Json(name = "rubrics") val rubrics : List<RubricData>
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(RubricData)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(rubrics)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RubricsResource> {
        override fun createFromParcel(parcel: Parcel): RubricsResource {
            return RubricsResource(parcel)
        }

        override fun newArray(size: Int): Array<RubricsResource?> {
            return arrayOfNulls(size)
        }
    }
}