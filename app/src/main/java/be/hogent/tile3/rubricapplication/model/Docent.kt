package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

@Entity(
    tableName = "docent_table",
    foreignKeys = [ForeignKey(
        entity = OpleidingsOnderdeel::class,
        parentColumns = arrayOf("opleidingsOnderdeelId"),
        childColumns = arrayOf("opleidingsOnderdeelId")
    )],
    indices = [Index("opleidingsOnderdeelId")]
)
data class Docent(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="docentId")
    val docentId: Long = 0,

    @ColumnInfo(name = "naam")
    val naam: String?,

    @ColumnInfo(name = "opleidingsOnderdeelId")
    val opleidingsOnderdeelId: Long
) :Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(docentId)
        parcel.writeString(naam)
        parcel.writeLong(opleidingsOnderdeelId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Docent> {
        override fun createFromParcel(parcel: Parcel): Docent {
            return Docent(parcel)
        }

        override fun newArray(size: Int): Array<Docent?> {
            return arrayOfNulls(size)
        }
    }

}