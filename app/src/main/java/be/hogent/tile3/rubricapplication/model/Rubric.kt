package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*

/**
 * Model class for [Rubric]
 * @constructor Creates a [Rubric] object from [Parcel]
 * @property rubricId                   ID for [Rubric]
 * @property onderwerp                  Subject for [Rubric]
 * @property omschrijving               Description for [Rubric]
 * @property datumTijdCreatie           Date when [Rubric] was created
 * @property datumTijdLaatsteWijziging  Date when [Rubric] was last edited
 */
@Entity(
    tableName = "rubric_table",
    /*foreignKeys = [ForeignKey(
        entity = OpleidingsOnderdeel::class,
        parentColumns = arrayOf("opleidingsOnderdeelId"),
        childColumns = arrayOf("opleidingsOnderdeelId"),
        onDelete = ForeignKey.SET_NULL)],*/
    indices = [Index("opleidingsOnderdeelId")]
)
data class Rubric(
    @PrimaryKey
    @ColumnInfo(name = "rubricId") val rubricId: Long = 0,
    @ColumnInfo(name = "onderwerp") val onderwerp: String? = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String? = "",
    @ColumnInfo(name = "datumTijdCreatie") val datumTijdCreatie: String? = "",
    @ColumnInfo(name = "datumTijdLaatsteWijziging") val datumTijdLaatsteWijziging: String? = "",
    @ColumnInfo(name = "opleidingsOnderdeelId") val opleidingsOnderdeelId: Long?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    )
    /**
     * Function to write a [Rubric] to a [Parcel]-object
     * @param parcel [Parcel]
     * @param flags [Int]
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(rubricId)
        parcel.writeString(onderwerp)
        parcel.writeString(omschrijving)
        parcel.writeString(datumTijdCreatie)
        parcel.writeString(datumTijdLaatsteWijziging)
        parcel.writeLong(opleidingsOnderdeelId!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rubric> {
        override fun createFromParcel(parcel: Parcel): Rubric {
            return Rubric(parcel)
        }

        override fun newArray(size: Int): Array<Rubric?> {
            return arrayOfNulls(size)
        }
    }
}