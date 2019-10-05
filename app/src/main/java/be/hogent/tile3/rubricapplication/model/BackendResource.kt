package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import java.io.Serializable

/**
 * Model for a BackendResource - The model we receive from the backend API
 */
class TestResource() : Parcelable, Serializable{
    @field:Json(name = "message") val message: String = ""

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TestResource> {
        override fun createFromParcel(parcel: Parcel): TestResource {
            return TestResource(parcel)
        }

        override fun newArray(size: Int): Array<TestResource?> {
            return arrayOfNulls(size)
        }
    }
}
