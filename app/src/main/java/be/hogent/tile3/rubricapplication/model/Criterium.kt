package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Rubric::class,
        parentColumns = arrayOf("rubricId"),
        childColumns = arrayOf("rubricId"),
        onDelete = CASCADE)],
    indices = [Index(value = ["criteriumId"])],
    tableName = "criterium_table"
)
/**
 * Model class for [Criterium]
 * @constructor Creates a [Criterium] class from a [Parcel]-object
 * @property naam               Name for [Criterium]
 * @property omschrijving       Description for [Criterium]
 * @property gewicht            Weight for [Criterium] for calculating the evaluation-score
 * @property criteriumGroepId   ID for defining the criteriumgroup the [Criterium] belongs to
 * @property rubricId           ID ([Rubric]) for defining the [Rubric] the [Criterium] belongs to
 * @see Parcelable
 * @see Serializable
  */
data class Criterium(
    // let op: geen criteriumgroepid
    @PrimaryKey @ColumnInfo(name = "criteriumId") val criteriumId: Long,
    @ColumnInfo(name = "naam") val naam: String? = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String? = "",
    @ColumnInfo(name = "gewicht") val gewicht: Double = 0.0,
    @ColumnInfo(name = "criteriumGroepId") val criteriumGroepId: Long,
    @ColumnInfo(name = "rubricId") val rubricId: Long
) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }
    /**
     * Function to write a [Criterium] to a [Parcel]-object
     * @param parcel [Parcel]
     * @param flags [Int]
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(criteriumId)
        parcel.writeString(naam)
        parcel.writeString(omschrijving)
        parcel.writeDouble(gewicht)
        parcel.writeLong(criteriumGroepId)
        parcel.writeLong(rubricId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Criterium> {
        override fun createFromParcel(parcel: Parcel): Criterium {
            return Criterium(parcel)
        }

        override fun newArray(size: Int): Array<Criterium?> {
            return arrayOfNulls(size)
        }
    }
}