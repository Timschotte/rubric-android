package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import java.util.*

@Entity( tableName = "student_table")
data class Student(

    @PrimaryKey @ColumnInfo(name = "studentId")
    val studentId: Long,

    @ColumnInfo(name = "studentNaam")
    val studentNaam: String,

    @ColumnInfo(name = "studentAchternaam")
    val studentAchternaam: String?,

    @ColumnInfo(name = "studentVoornaam")
    val studentVoornaam: String?,

    @ColumnInfo(name = "studentGeboortedatum")
    val studentGeboortedatum: String?,

    @ColumnInfo(name = "studentNr")
    val studentenNr: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(studentId)
        parcel.writeString(studentNaam)
        parcel.writeString(studentAchternaam)
        parcel.writeString(studentVoornaam)
        parcel.writeString(studentGeboortedatum)
        parcel.writeString(studentenNr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Student> {
        override fun createFromParcel(parcel: Parcel): Student {
            return Student(parcel)
        }

        override fun newArray(size: Int): Array<Student?> {
            return arrayOfNulls(size)
        }
    }
}