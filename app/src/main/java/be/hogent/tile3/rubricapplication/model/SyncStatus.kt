package be.hogent.tile3.rubricapplication.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "status_table")
data class SyncStatus(
    @PrimaryKey var id: Short,
    @ColumnInfo(name = "connected") var connectionActive : Boolean,
    @ColumnInfo(name = "timestamp") var lastSyncTimeStamp : String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt().toShort(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id.toInt())
        parcel.writeByte(if (connectionActive) 1 else 0)
        parcel.writeString(lastSyncTimeStamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SyncStatus> {
        override fun createFromParcel(parcel: Parcel): SyncStatus {
            return SyncStatus(parcel)
        }

        override fun newArray(size: Int): Array<SyncStatus?> {
            return arrayOfNulls(size)
        }
    }

}