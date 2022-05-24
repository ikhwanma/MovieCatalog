package ikhwan.binar.binarchallengelima.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Entity
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "id_movie") var idMovie: Int?,
    @ColumnInfo(name = "img_movie") var imgMovie: String?
): Parcelable
