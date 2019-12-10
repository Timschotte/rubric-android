package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.squareup.moshi.Json
import java.io.Serializable
/**
 * Model class for [Niveau]
 * @constructor Creates a [Niveau] class from a [Parcel]-object
 * @property niveauId               ID for [Niveau]
 * @property titel                  Title for [Niveau]
 * @property omschrijving           Description for [Niveau]
 * @property ondergrens             Lowest score available for this [Niveau]
 * @property bovengrens             Highest score available for this [Niveau]
 * @property volgnummer             In a list of [Niveau], this number will indicate the [Niveau]'s position.
 * @property rubricId               ID ([Rubric]) the [Niveau] belongs to
 * @property criteriumGroepId       ID (CriteriumGroup) the [Niveau] belongs to
 * @property criteriumId            ID ([Criterium]) the [Niveau] belongs to
 * @see Parcelable
 * @see Serializable
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Criterium::class,
        parentColumns = arrayOf("criteriumId"),
        childColumns = arrayOf("criteriumId"), onDelete = CASCADE)],
    indices = [Index(value = ["niveauId"])],
    tableName = "niveau_table"
)
data class Niveau(
    @PrimaryKey @ColumnInfo(name = "niveauId") val niveauId: Long = 0L,
    @ColumnInfo(name = "titel") val titel: String? = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String? = "",
    @ColumnInfo(name = "ondergrens") val ondergrens: Int = 0,
    @ColumnInfo(name = "bovengrens") val bovengrens: Int = 0,
    @ColumnInfo(name = "volgnummer") val volgnummer: Int,
    @ColumnInfo(name="rubricId") val rubricId: Long,
    @ColumnInfo(name="criteriumGroepId") val criteriumGroepId: Long,
    @ColumnInfo(name = "criteriumId") val criteriumId: Long
) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }
    /**
     * Function to write a [Niveau] to a [Parcel]-object
     * @param parcel [Parcel]
     * @param flags [Int]
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(niveauId)
        parcel.writeString(titel)
        parcel.writeString(omschrijving)
        parcel.writeInt(ondergrens)
        parcel.writeInt(bovengrens)
        parcel.writeInt(volgnummer)
        parcel.writeLong(rubricId)
        parcel.writeLong(criteriumGroepId)
        parcel.writeLong(criteriumId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Niveau> {
        override fun createFromParcel(parcel: Parcel): Niveau {
            return Niveau(parcel)
        }

        override fun newArray(size: Int): Array<Niveau?> {
            return arrayOfNulls(size)
        }
    }
    /**
     * Function to give a String representation of the [Niveau] object
     */
    override fun toString() = "Niveau ${niveauId}: ${titel} voor criterium ${criteriumId} met waarde $ondergrens-$bovengrens, positie $volgnummer"
}