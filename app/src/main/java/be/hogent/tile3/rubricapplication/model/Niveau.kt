package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json
import java.io.Serializable

@Entity(
    foreignKeys = arrayOf(
    ForeignKey(
        entity = Criterium::class,
        parentColumns = arrayOf("criteriumId"),
        childColumns = arrayOf("criteriumId"))
),  indices = arrayOf(Index(value = ["niveauId"])),
    tableName = "niveau_table"
)
data class Niveau(
    @PrimaryKey @ColumnInfo(name = "niveauId") val niveauId: Long = 0L,
    @ColumnInfo(name = "titel") val titel: String = "",
    @ColumnInfo(name = "omschrijving") val omschrijving: String = "",
    @ColumnInfo(name = "ondergrens") val ondergrens: Int = 0,
    @ColumnInfo(name = "bovengrens") val bovengrens: Int = 0,
    @ColumnInfo(name = "volgnummer") val volgnummer: Int,
    @ColumnInfo(name="rubricId") val rubricId: String,
    @ColumnInfo(name="criteriumGroepId") val criteriumGroepId: String,
    @ColumnInfo(name = "criteriumId") val criteriumId: String
) : Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(niveauId)
        parcel.writeString(titel)
        parcel.writeString(omschrijving)
        parcel.writeInt(ondergrens)
        parcel.writeInt(bovengrens)
        parcel.writeInt(volgnummer)
        parcel.writeString(rubricId)
        parcel.writeString(criteriumGroepId)
        parcel.writeString(criteriumId)
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

    override fun toString() = "Niveau ${niveauId}: ${titel} voor criterium ${criteriumId} met waarde $ondergrens-$bovengrens, positie $volgnummer"
}