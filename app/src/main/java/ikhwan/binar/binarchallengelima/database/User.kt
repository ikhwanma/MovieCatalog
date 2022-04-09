package ikhwan.binar.binarchallengelima.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class User(
    @PrimaryKey var email: String,
    @ColumnInfo(name = "name") var nama: String,
    @ColumnInfo(name = "full_name") var fullNama: String?,
    @ColumnInfo(name = "tanggal_lahir") var tglLahir: String?,
    @ColumnInfo(name = "alamat") var alamat: String?,
    @ColumnInfo(name = "password") var password: String
) : Parcelable